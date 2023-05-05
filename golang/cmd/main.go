package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
)

// UrlEntry A struct to represent an url entry in the json file
type UrlEntry struct {
	Url    string      `json:"url"`
	Param  string      `json:"param"`
	Method string      `json:"method"`
	Code   int         `json:"code"`
	Type   string      `json:"type"`
	Data   interface{} `json:"data"`
}

// A global variable to store the url entries from the json file
var urlEntries []UrlEntry

// A function to load the json file into memory
func loadJsonFile(filename string) error {
	// Read the file content
	data, err := os.ReadFile(filename)
	if err != nil {
		return err
	}
	// Parse the json data into urlEntries
	err = json.Unmarshal(data, &urlEntries)
	if err != nil {
		return err
	}
	return nil
}

// A function to handle requests and respond with the data element of the matched url
func handleRequest(w http.ResponseWriter, r *http.Request) {
	// initialize urlEntries
	urlEntries = []UrlEntry{}
	// Load the json file
	readFileErr := loadJsonFile("db.json")
	if readFileErr != nil {
		log.Fatal(readFileErr)
	}

	// get origin and set to response header if exist
	origin := r.Header.Get("Origin")
	if origin != "" {
		w.Header().Set("Access-Control-Allow-Origin", origin)
	} else {
		w.Header().Set("Access-Control-Allow-Origin", "*")
	}
	// set Access-Control-Allow-Credentials to true
	w.Header().Set("Access-Control-Allow-Credentials", "true")
	// set Access-Control-Allow-Methods to GET, POST, PUT, DELETE, OPTIONS
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	// set Access-Control-Max-Age to 86400
	w.Header().Set("Access-Control-Max-Age", "86400")
	// get access control request headers and set to response headers if exist
	accessControlRequestHeaders := r.Header.Get("Access-Control-Request-Headers")
	if accessControlRequestHeaders != "" {
		w.Header().Set("Access-Control-Allow-Headers", accessControlRequestHeaders)
	} else {
		w.Header().Set("Access-Control-Allow-Headers", "*")
	}
	// preflight request with options method need to be allowed
	if r.Method == "OPTIONS" {
		// response 204 no content
		w.WriteHeader(http.StatusNoContent)
		return
	}

	responseEntry := UrlEntry{}

	// Loop through the urlEntries to find a match
	for _, entry := range urlEntries {
		// Check if the request url and method match the entry
		if r.URL.Path == entry.Url && r.Method == entry.Method && (entry.Param == "" || r.URL.RawQuery == entry.Param) {
			responseEntry = entry
			break
		}
	}

	// if responseEntry is empty, return 404 not found
	if responseEntry.Url == "" {
		// If no match is found, respond with 404 not found
		w.WriteHeader(http.StatusNotFound)
		_, err := fmt.Fprintln(w, "Not Found")
		if err != nil {
			return
		}
		return
	}
	// Set the response status code and header
	w.WriteHeader(responseEntry.Code)
	if responseEntry.Type != "" {
		w.Header().Set("Content-Type", responseEntry.Type)
		// read a png file as stream and pipe to response body

		file, fileOpenErr := os.Open(responseEntry.Data.(string))
		if fileOpenErr != nil {
			log.Println(fileOpenErr)
			return
		}
		defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				log.Println(err)
				return
			}
		}(file)
		stat, _ := file.Stat()
		// stream the file to response body
		http.ServeContent(w, r, responseEntry.Data.(string), stat.ModTime(), file)
		return

	} else {
		w.Header().Set("Content-Type", "application/json")
		// Encode the data element as json and write it to the response body
		responseErr := json.NewEncoder(w).Encode(responseEntry.Data)
		if responseErr != nil {
			log.Println(responseErr)
		}
		return
	}
}

func main() {
	// Register the handler function for all requests
	http.HandleFunc("/", handleRequest)

	// Start the server and listen on port 8080
	log.Println("Server is running on port 8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}
