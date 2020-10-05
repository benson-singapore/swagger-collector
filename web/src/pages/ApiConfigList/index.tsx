import React, { ReactText, useRef, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable, { ActionType } from '@ant-design/pro-table';
import { history } from 'umi';
import { deleteAPiConfig, getApiConfigList } from './ApiConfig';
import { ApiConfig } from './API_ApiConfig';
import { Button, message, Popconfirm, Spin, Switch } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import ApiConfigAdd from '@/pages/ApiConfigList/components/ApiConfigAdd';
import ApiConfigEdit from './components/ApiConfigEdit';

/**
 * IP配置列表
 *
 * @author zhangby
 * @date 1/9/20 6:44 pm
 */
export default (): React.ReactNode => {
  const [rows, setRows] = useState<ReactText[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

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
    <div>
      <Spin spinning={loading}>
        <PageContainer subTitle="API 接口地址管理">
          <ProTable<ApiConfig>
            actionRef={ref}
            columns={[
              {
                title: '接口分组',
                dataIndex: 'name',
                width: 150,
                render: (_, record) => (
                  <Button
                    type="link"
                    style={{ paddingLeft: 0 }}
                    onClick={() =>
                      history.push(
                        '/api/config/detail/' +
                          record.id +
                          '/' +
                          (record.name + '（ ' + record.value + ' ）'),
                      )
                    }
                  >
                    {record.name}
                  </Button>
                ),
              },
              {
                title: '分组类型',
                dataIndex: 'value',
                width: 150,
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
              },
              {
                title: '操作',
                dataIndex: 'option',
                valueType: 'option',
                align: 'center',
                width: 150,
                render: (_, record) => (
                  <>
                    <ApiConfigEdit refresh={() => ref.current?.reload()} apiConfigId={record.id} />
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
            request={(params, sorter, filter) => getApiConfigList({ ...params, sorter, filter })}
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
              <ApiConfigAdd refresh={() => ref.current?.reload()} />,
            ]}
          />
        </PageContainer>
      </Spin>
    </div>
  );
};
