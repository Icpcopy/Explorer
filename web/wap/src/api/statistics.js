
import request from '@/utils/request'

export function getAddressByAssets(data) {
  return request({
    url: '/api/address/getAddressByAssets',
    method: 'get',
    params:data
  })
}
export function getCoinInfo(data) {
    return request({
      url: '/api/address/getCoinInfo',
      method: 'get',
      params:data
    })
  }

  export function getCoinRange(data) {
    return request({
      url: '/api/address/getCoinRange',
      method: 'get',
      params:data
    })
  }



