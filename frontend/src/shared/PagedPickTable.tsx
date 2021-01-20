import React, { useCallback, useEffect, useState } from 'react';
import { Table, Input } from 'antd';

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
}> = ({
  dataSource = [],
  changePage = () => {},
  modify = false,
  value = null,
  onChange = () => {},
  initVals = [],
}) => {
  const [filterText] = useState('');
  const [selected, setSelected] = useState<DataSource[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    if (value == null) return;

    setSelected(
      value
        .map((v) => {
          const idx = initVals.map((iv) => iv.id).indexOf(v);
          return initVals[idx];
        })
        .filter((v) => v != null)
    );
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
      ) : null}
      {selected.map((sel) => (
        <Input disabled key={sel.id} value={sel.value} />
      ))}
    </div>
  );
};

export default PagedPickTable;
