import { request } from 'umi';

// 接口配置对象
export interface ApiConfig {
  id: string;
  parentId: string;
  autoAuthId: string;
  name: string;
  value: string;
  host: string;
  createDate: string;
  remarks: string;
  sort: number;
  status: string;
  tails: {
    statusEnum: {
      label: string;
      value: string;
    };
  };
}

// api 级联对象
export interface ApiConfigCascade {
  id: string;
  name: string;
  value: string;
  host: string;
  children: ApiConfigCascade[];
}

// 查询接口配置列表
export async function getApiConfigList(
  params: { current: number },
  sorter?: { [key: string]: any[] },
  filter?: { [key: string]: any },
) {
  let data = await request<API.ResultPoJo<API.Page<ApiConfig>>>('/swagger/api/config', {
    params: {
      ...params,
      pageNum: params.current,
      sorter,
      filter,
    },
  });
  let page = data.result;
  return {
    data: page?.records,
    current: page?.current,
    total: page?.total,
    success: '000' === data.code,
  };
}

// 新增接口配置
export async function AddApiConfig(params: {}) {
  return request<API.ResultPoJo<any>>('/swagger/api/config', {
    method: 'post',
    data: params,
  });
}

// 删除接口
export async function deleteAPiConfig(id: string) {
  return request<API.ResultPoJo<any>>('/swagger/api/config/' + id, {
    method: 'delete',
  });
}

// 获取详情
export async function getApiConfigById(id: string) {
  return request<API.ResultPoJo<ApiConfig>>('/swagger/api/config/' + id);
}

// 更新接口
export async function updateApiConfig(id: string, params: {}) {
  return request<API.ResultPoJo<any>>('/swagger/api/config/' + id, {
    method: 'put',
    data: params,
  });
}

// 获取最大排序值
export async function getApiConfigMaxSort(parentId?: string) {
  return request<API.ResultPoJo<number>>('/swagger/api/config/get/max/sort', {
    params: {
      parentId,
    },
  });
}

// 获取级联查询结果
export async function getApiConfigCascade() {
  return request<API.ResultPoJo<ApiConfigCascade[]>>('/swagger/api/config/get/cascade');
}
