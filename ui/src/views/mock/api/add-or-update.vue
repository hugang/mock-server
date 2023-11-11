<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
	    				<el-form-item label="url" prop="url">
					<el-input v-model="dataForm.url" placeholder="url"></el-input>
				</el-form-item>
					<el-form-item label="method" prop="method">
						<el-select v-model="dataForm.method" placeholder="请选择">
							<el-option label="GET" value="GET"></el-option>
							<el-option label="POST" value="POST"></el-option>
							<el-option label="PUT" value="PUT"></el-option>
							<el-option label="DELETE" value="DELETE"></el-option>
						</el-select>
					</el-form-item>
				<el-form-item label="code" prop="code">
					<el-input v-model="dataForm.code" placeholder="code"></el-input>
				</el-form-item>
					<el-form-item label="type" prop="type">
						<el-select v-model="dataForm.type" placeholder="请选择">
							<el-option label="JSON" value="JSON"></el-option>
							<el-option label="FILE" value="FILE"></el-option>
						</el-select>
					</el-form-item>
				<el-form-item label="data" prop="data">
					<el-input type="textarea" v-model="dataForm.data"></el-input>
				</el-form-item>
				<el-form-item label="param" prop="param">
					<el-input type="textarea" v-model="dataForm.param"></el-input>
				</el-form-item>
				<el-form-item label="添付ID" prop="attachmentId">
					<el-input v-model="dataForm.attachmentId" placeholder="添付ID"></el-input>
				</el-form-item>
				<el-form-item label="添付名" prop="attachmentName">
					<el-input v-model="dataForm.attachmentName" placeholder="添付名"></el-input>
				</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useApiApi, useApiSubmitApi } from '@/api/mock/api'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	id: '',
	url: '',
	method: '',
	code: '',
	type: '',
	data: '',
	param: '',
	attachmentId: '',
	attachmentName: ''})

const init = (id?: number) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	if (id) {
		getApi(id)
	}
}

const getApi = (id: number) => {
	useApiApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		useApiSubmitApi(dataForm).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					visible.value = false
					emit('refreshDataList')
				}
			})
		})
	})
}

defineExpose({
	init
})
</script>
