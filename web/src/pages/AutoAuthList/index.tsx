import React, { useState, ReactText, useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { AutoAuth, getAutoAuthList } from './AutoAuth';
import ProTable, { ActionType } from '@ant-design/pro-table';
import AutoAuthAdd from './components/AutoAuthAdd';
import AutoAuthEdit from './components/AutoAuthEdit';
import { Popconfirm, Button, message, Spin, Badge } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { deleteAutoAuth } from './AutoAuth';

/**
 * 自动授权管理
 */
export default (): React.ReactNode => {
  const [rows, setRows] = useState<ReactText[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const ref = useRef<ActionType>();

  const onDelete = async () => {
    try {
      if (rows.length == 0) {
        message.error('请选择要操作的记录');
      }
      setLoading(true);
      await deleteAutoAuth(rows.join(','));
      setRows([]);
      setLoading(false);
      ref.current?.reloadAndRest();
      message.success('删除记录成功！');
    } catch (e) {
      setLoading(false);
      message.error('操作失败请稍后重试！');
    }
  };

  return (
    <>
      <PageContainer subTitle="管理配置，接口自动 token 授权">
        <Spin spinning={loading}>
          <ProTable<AutoAuth>
            actionRef={ref}
            columns={[
              {
                title: '授权名称',
                dataIndex: 'name',
                width: 150,
                render: (_, record) => (
                  <AutoAuthEdit refresh={() => ref.current?.reload()} autoAuthId={record.id}>
                    <Button type="link" style={{ paddingLeft: 0 }}>
                      {record.name}
                    </Button>
                  </AutoAuthEdit>
                ),
              },
              {
                title: '访问地址',
                dataIndex: 'url',
                hideInSearch: true,
                width: 150,
              },
              {
                title: '请求方式',
                dataIndex: 'method',
                align: 'center',
                width: 150,
                hideInSearch: true,
                render: (_, record) => <span>{record.tails.methodEnum.label}</span>,
              },
              {
                title: '状态',
                dataIndex: 'status',
                align: 'center',
                width: 150,
                valueEnum: {
                  '0': {
                    text: '正常',
                    status: 'success',
                  },
                  '1': {
                    text: '禁用',
                    status: 'error',
                  },
                },
                render: (_, record) => (
                  <span>
                    {record.status === '0' ? (
                      <span>
                        <Badge
                          status="processing"
                          style={{ color: '#008dff' }}
                          text={record.tails.statusEnum.label}
                        />
                      </span>
                    ) : (
                      <Badge
                        status="error"
                        style={{ color: '#f50' }}
                        text={record.tails.statusEnum.label}
                      />
                    )}
                  </span>
                ),
              },
              {
                title: '排序',
                dataIndex: 'sort',
                width: 50,
                hideInSearch: true,
              },
              {
                title: '登录名',
                align: 'center',
                dataIndex: 'username',
                hideInSearch: true,
                width: 150,
              },
              {
                title: '操作',
                dataIndex: 'option',
                valueType: 'option',
                align: 'center',
                width: 150,
                render: (_, record) => (
                  <>
                    <AutoAuthEdit refresh={() => ref.current?.reload()} autoAuthId={record.id} />
                    <Popconfirm
                      placement="topRight"
                      title="确定要删除选中的记录吗?"
                      onConfirm={async () => {
                        try {
                          setLoading(true);
                          await deleteAutoAuth(record.id);
                          setLoading(false);
                          message.success('删除记录成功！');
                          ref.current?.reload();
                        } catch (e) {
                          setLoading(false);
                        }
                      }}
                      okText="Yes"
                      cancelText="No"
                    >
                      <Button type={'primary'} shape="circle" danger style={{ marginLeft: 10 }}>
                        <DeleteOutlined />
                      </Button>
                    </Popconfirm>
                  </>
                ),
              },
            ]}
            rowSelection={{
              onChange: (selectedRowKeys, _) => setRows(selectedRowKeys),
            }}
            request={(params, sorter, filter) => getAutoAuthList({ ...params, sorter, filter })}
            pagination={{
              pageSize: 10,
            }}
            rowKey="id"
            headerTitle="API 数据配置列表"
            toolBarRender={() => [
              <div key="del">
                {rows.length > 0 ? (
                  <Popconfirm
                    title="确定要删除选中的记录吗?"
                    onConfirm={onDelete}
                    okText="Yes"
                    cancelText="No"
                  >
                    <Button type="primary" danger>
                      <DeleteOutlined />
                      批量删除
                    </Button>
                  </Popconfirm>
                ) : null}
              </div>,
              <AutoAuthAdd refresh={() => ref.current?.reload()} />,
            ]}
          />
        </Spin>
      </PageContainer>
    </>
  );
};
