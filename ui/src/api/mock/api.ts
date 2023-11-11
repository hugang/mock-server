import service from '@/utils/request'

export const useApiApi = (id: number) => {
	return service.get('/mock/api/' + id)
}

export const useApiSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/mock/api', dataForm)
	} else {
		return service.post('/mock/api', dataForm)
	}
}

