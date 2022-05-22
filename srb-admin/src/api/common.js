import request from '@/utils/request'

export default {
  getCount() {
    return request({
      url: '/common/getCount',
      method: 'get',
    })
  },
  openDir(){
    return request({
      url: '/common/openDir',
      method: 'get',
    })
  },
  executePython(preName,fileName,flag){
    return request({
      url: '/common/executePython',
      method: 'get',
      params: { 'preName': preName, 'fileName':fileName, 'flag':flag}
    })
  }
}
