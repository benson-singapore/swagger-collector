import React, { useState, useRef, ReactText } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { history } from 'umi';
import ProTable, { ActionType } from '@ant-design/pro-table';
import { ApiConfig } from '@/pages/ApiConfigList/API_ApiConfig';
import ApiConfigDetailEdit from '../ApiConfigDetailList/components/ApiConfigDetailEdit';
import { deleteAPiConfig, getApiConfigList } from '@/pages/ApiConfigList/ApiConfig';
import ApiConfigDetailAdd from './components/ApiConfigDetailAdd';
import { message, Switch, Popconfirm, Button, Tag, Spin, Typography } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';

const { Paragraph } = Typography;

/**
 * 接口配置详情列表
 */
export default (props: any): React.ReactNode => {
  const [rows, setRows] = useState<ReactText[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const { apiConfigId, apiConfigName } = props.match.params;

  const ref = useRef<ActionType>();

  // 删除
  const onDelete = async () => {
    try {
      if (rows.length == 0) {
        message.error('请选择要操作的记录');
      }
      setLoading(true);
      await deleteAPiConfig(rows.join(','));
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
      <Spin spinning={loading}>
        <PageContainer
          onBack={() => history.push('/api/config')}
          title={
            <div>
              <span>接口配置详情</span>
              <span style={{ fontSize: 14, fontWeight: 400, marginLeft: 20 }}>{apiConfigName}</span>
            </div>
          }
        >
          <ProTable<ApiConfig>
            actionRef={ref}
            columns={[
              {
                title: '接口名称',
                dataIndex: 'name',
                width: 150,
              },
              {
                title: '服务地址',
                dataIndex: 'host',
                width: 150,
                render: (_, record) => (
                  <div style={{ width: 200 }}>
                    <Paragraph ellipsis style={{ marginBottom: 0 }}>
                      {record.host}
                    </Paragraph>
                  </div>
                ),
              },
              {
                title: '接口地址',
                dataIndex: 'value',
                width: 150,
                render: (_, record) => (
                  <div style={{ width: 200 }}>
                    <Paragraph ellipsis style={{ marginBottom: 0 }}>
                      {record.value}
                    </Paragraph>
                  </div>
                ),
              },
              {
                title: '备注',
                dataIndex: 'remarks',
                width: 150,
              },
              {
                title: '状态',
                dataIndex: 'sort',
                width: 50,
                hideInSearch: true,
                render: (_, record) => (
                  <>
                    <Switch size="small" checked={record.status === '0'} />
                  </>
                ),
              },
              {
                title: '排序',
                dataIndex: 'sort',
                width: 50,
                hideInSearch: true,
              },
              {
                title: '创建时间',
                dataIndex: 'createDate',
                width: 150,
                valueType: 'dateTime',
                hideInSearch: true,
                render: (_, record) => (
                  <>
                    <Tag.CheckableTag style={{ paddingLeft: 0 }}>
                      {record.createDate}
                    </Tag.CheckableTag>
                  </>
                ),
              },
              {
                title: '操作',
                dataIndex: 'option',
                valueType: 'option',
                align: 'center',
                width: 150,
                render: (_, record) => (
                  <>
                    <ApiConfigDetailEdit
                      parentName={apiConfigName}
                      refresh={() => ref.current?.reload()}
                      apiConfigId={record.id}
                    />
                    <Popconfirm
                      placement="topRight"
                      title="确定要删除选中的记录吗?"
                      onConfirm={async () => {
                        try {
                          setLoading(true);
                          await deleteAPiConfig(record.id);
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
            request={(params, sorter, filter) =>
              getApiConfigList({
                ...{ ...params, parentId: apiConfigId },
                sorter,
                filter,
              })
            }
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
              <ApiConfigDetailAdd
                refresh={() => ref.current?.reload()}
                parentId={apiConfigId}
                parentName={apiConfigName}
              />,
            ]}
          />
        </PageContainer>
      </Spin>
    </>
  );
};
