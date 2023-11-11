<template>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.name" placeholder="添付ファイル名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-upload class="upload-demo" ref="upload" action="/mock/attachment" :auto-upload="false"
                   :on-change="handleUploadChange" :show-file-list="false">
          <el-button type="primary">上传文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
              @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="id" label="id" header-align="center" align="center"></el-table-column>
      <el-table-column prop="name" label="添付ファイル名称" header-align="center" align="center"></el-table-column>
      <el-table-column prop="url" label="添付ファイルアドレス" header-align="center" align="center"></el-table-column>
      <el-table-column prop="size" label="添付ファイルサイズ" header-align="center" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template #default="scope">
          <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="state.pageSizes" :page-size="state.limit"
                   :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
                   @current-change="currentChangeHandle"></el-pagination>

  </el-card>
</template>

<script setup lang="ts" name="MockAttachmentIndex">
import {useCrud} from '@/hooks'
import {reactive} from 'vue'
import {IHooksOptions} from '@/hooks/interface'
import {saveAttachment} from '@/api/mock/attachment'

const state: IHooksOptions = reactive({
  dataListUrl: '/mock/attachment/page',
  deleteUrl: '/mock/attachment',
  queryForm: {
    name: ''
  }
})

const handleUploadChange = (file: any, fileList: any) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  saveAttachment(formData).then((res: any) => {
    if (res.data.code === 0) {
      getDataList()
    }
  })
}

const {getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle} = useCrud(state)
</script>
