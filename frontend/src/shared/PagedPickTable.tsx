import React, { useCallback, useEffect, useState } from 'react';
import { Table, Input, List } from 'antd';
import Search from 'antd/lib/input/Search';

const columns = [
  {
    title: 'Id',
    dataIndex: 'id',
  },
  {
    title: 'Value',
    dataIndex: 'value',
  },
];

type DataSource = { id: number; value: string };

const PagedPickTable: React.FunctionComponent<{
  dataSource: DataSource[];
  changePage: (page: number, filter: string) => void;
  modify: boolean;
  value?: number[];
  onChange?: (arg: number[]) => void;
  initVals?: DataSource[];
  onSearch?: (arg: string) => void;
}> = ({
  dataSource = [],
  changePage = () => {},
  modify = false,
  value = null,
  onChange = () => {},
  initVals = [],
  onSearch = null,
}) => {
  const [filterText] = useState('');
  const [selected, setSelected] = useState<DataSource[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    if (value == null) return;

    setSelected((old) => [
      ...old,
      ...(value
        .map((v) => {
          if (old.map((o) => o.id).includes(v)) return null;

          const idx = initVals.map((iv) => iv.id).indexOf(v);
          return initVals[idx] ?? { id: v, value: '' };
        })
        .filter((v) => v != null) as DataSource[]),
    ]);
    setSelectedRowKeys(value);
  }, [value, initVals]);

  const onSelChange = useCallback(
    (newSelectedRowKeys: React.Key[], selectedRows: DataSource[]) => {
      onChange(selectedRows.map((k) => k.id));

      setSelected(selectedRows);
      setSelectedRowKeys(newSelectedRowKeys);
    },
    [onChange]
  );

  return (
    <div>
      {modify ? (
        <>
          {onSearch == null ? null : (
            <Search
              placeholder="filter"
              onSearch={onSearch}
              style={{ width: 200 }}
            />
          )}
          <Table
            rowKey={(record) => record.id}
            rowSelection={{
              type: 'checkbox',
              selectedRowKeys,
              onChange: onSelChange,
            }}
            pagination={{
              position: ['topRight'],
              onChange: (page) => {
                changePage(page - 1, filterText);
              },
            }}
            columns={columns}
            dataSource={dataSource}
          />
        </>
      ) : null}
      <List
        bordered
        dataSource={selected}
        renderItem={(sel: DataSource) => (
          <List.Item key={sel.id}>{sel.value}</List.Item>
        )}
      />
    </div>
  );
};

export default PagedPickTable;
