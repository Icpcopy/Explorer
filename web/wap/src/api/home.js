import request from '@/utils/request'


export function getInfo() {
  return request({
    url: '/api/resource/pack/list',
    method: 'get',
  })
}

export function getChainName() {
    return request({
      url: '/api/chain/getChainName',
      method: 'get',
    })
  }
