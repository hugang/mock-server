import service, {serviceCustom} from '@/utils/request'

export const useAttachmentApi = (id: number) => {
	return service.get('/mock/attachment/' + id)
}

export const useAttachmentSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/mock/attachment', dataForm)
	} else {
		return service.post('/mock/attachment', dataForm)
	}
}

export const saveAttachment = (dataForm: any) => {
	return serviceCustom.post('/mock/attachment', dataForm,{
		headers: { 'Content-Type': 'multipart/form-data' }
	})
}
