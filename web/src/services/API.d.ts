declare namespace API {
  export interface CurrentUser {
    avatar?: string;
    name?: string;
    title?: string;
    group?: string;
    signature?: string;
    tags?: {
      key: string;
      label: string;
    }[];
    userid?: string;
    access?: 'user' | 'guest' | 'admin';
    unreadCount?: number;
  }

  export interface LoginStateType {
    status?: 'ok' | 'error';
    type?: string;
  }

  export interface NoticeIconData {
    id: string;
    key: string;
    avatar: string;
    title: string;
    datetime: string;
    type: string;
    read?: boolean;
    description: string;
    clickClose?: boolean;
    extra: any;
    status: string;
  }

  // 返回对象
  export interface ResultPoJo<T> {
    code?: string;
    msg?: string;
    result?: T;
  }

  // 分页对象
  export interface Page<T> {
    current: number;
    orders: number[];
    pages: number;
    records: T[];
    searchCount: boolean;
    size: number;
    total: 0;
  }

  // 字典对象
  interface Dict {
    label: string;
    value: string;
  }
}
