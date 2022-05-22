import request from '@/utils/request'

export default {
  pageRecords(page, size,sortName) {
    return request({
      url: '/apis/selectByPage',
      method: 'get',
      params: { 'page': page, 'size': size,'sortName': sortName }
    })
  },
  getCategoriesCount(num) {
    return request({
      url: '/apis/groupQuery',
      method: 'get',
      params: { 'num': num,  }
    })
  },
  searchLikeName(name,value,page,size,sortName) {
    return request({
      url: '/apis/searchLikeName',
      method: 'get',
      params:{'name': name,'value':value,'page':page,'size':size,'sortName':sortName}
    })
  },
  selectTag(num) {
    return request({
      url: '/apis/selectTag',
      method: 'get',
      params:{'num': num}
    })
  },
}
