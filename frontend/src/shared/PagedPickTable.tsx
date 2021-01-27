import React, {
  PropsWithChildren,
  useCallback,
  useEffect,
  useState,
} from 'react';
import { Table, List } from 'antd';
import Search from 'antd/lib/input/Search';
import { ColumnsType } from 'antd/lib/table';

interface Props<ObjectType> {
  dataSource: ObjectType[];
  changePage: (page: number, filter: string) => void;
  modify: boolean;
  value?: number[];
  onChange?: (arg: number[]) => void;
  initVals?: ObjectType[];
  onSearch?: (arg: string) => void;
  columns?: ColumnsType<ObjectType>;
  type?: 'checkbox' | 'radio';
}

const PagedPickTable = <T extends { id: number; value: string }>({
  dataSource = [],
  changePage = () => {},
  modify = false,
  value,
  onChange = () => {},
  initVals = [],
  onSearch,
  columns = [
    {
      title: 'Id',
      dataIndex: 'id',
    },
    {
      title: 'Value',
      dataIndex: 'value',
    },
  ],
  type = 'checkbox',
}: PropsWithChildren<Props<T>>): React.ReactElement => {
  const [filterText] = useState('');
  const [selected, setSelected] = useState<T[]>([]);
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
        .filter((v) => v != null) as T[]),
    ]);
    setSelectedRowKeys(value);
  }, [value, initVals]);

  const onSelChange = useCallback(
    (newSelectedRowKeys: React.Key[], selectedRows: T[]) => {
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
              type,
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
        renderItem={(sel: T) => <List.Item key={sel.id}>{sel.value}</List.Item>}
      />
    </div>
  );
};

export default PagedPickTable;
