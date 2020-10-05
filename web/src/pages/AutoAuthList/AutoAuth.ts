import { request } from 'umi';
import { message } from 'antd';

// 枚举
export enum MethodEnum {
  'GET' = '1',
  'POST' = '2',
  'PUT' = '3',
  'PATCH' = '4',
  'DELETE' = '5',
}

// 自动授权对象
export interface AutoAuth {
  id: string;
  name: string;
  url: string;
  username: string;
  password: string;
  method: MethodEnum;
  tokenKey: string;
  status: number;
  tails: {
    methodEnum: {
      label: string;
      value: string;
    };
    statusEnum: {
      label: string;
      value: string;
    };
  };
}

// 获取请求方式枚举
export function getMethodDictList(): API.Dict[] {
  const dictList = [];
  for (let methodEnumKey in MethodEnum) {
    dictList.push({ label: methodEnumKey, value: MethodEnum[methodEnumKey] });
  }
  return dictList;
}

// 查询接口配置列表
export async function getAutoAuthList(
  params: { current: number },
  sorter?: { [key: string]: any[] },
  filter?: { [key: string]: any },
) {
  let data = await request<API.ResultPoJo<API.Page<AutoAuth>>>('/swagger/api/auto/auth', {
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

// 新增自动授权
export async function addAutoAuth(record: AutoAuth) {
  return request<API.ResultPoJo<any>>('/swagger/api/auto/auth', {
    method: 'post',
    data: record,
  });
}

// 删除自动授权
export async function deleteAutoAuth(id: string) {
  return request<API.ResultPoJo<any>>('/swagger/api/auto/auth/' + id, {
    method: 'delete',
  });
}

// 查询详情
export async function getAutoAuthById(id: string) {
  return request<API.ResultPoJo<AutoAuth>>('/swagger/api/auto/auth/' + id);
}

// 更新自动授权
export async function updateAutoAuth(record: AutoAuth) {
  return request('/swagger/api/auto/auth/' + record.id, {
    method: 'put',
    data: record,
  });
}

// 获取最大排序值
export async function getMaxSort() {
  return request<API.ResultPoJo<number>>('/swagger/api/auto/auth/get/max/sort');
}

// 获取选择下拉框
export async function getSelectData() {
  return request<API.ResultPoJo<API.Dict[]>>('/swagger/api/auto/auth/get/select/data');
}

// 调用授权
export async function getAuthorization(record: AutoAuth) {
  // 判断
  if (!record.url) {
    message.error('访问地址不能为空');
  }
  if (!record.method) {
    message.error('请求方式不能为空');
  }

  if (record.method === '1') {
    return request(record.url, {
      params: record,
    });
  } else {
    return request(record.url, {
      method: record.method,
      data: record,
    });
  }
}

// 获取token
export async function getToken(autoAuth: AutoAuth) {
  try {
    const result = await getAuthorization(autoAuth);
    // 格式化数据
    if (autoAuth.tokenKey) {
      let res = result;
      autoAuth.tokenKey.split('.').forEach((key) => {
        res = res[key];
      });
      return res;
    }
  } catch (e) {
    return undefined;
  }
}
