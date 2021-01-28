import React from 'react';
import { Table } from 'antd';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function TermCodeNameTable(props: { data: any[] }): JSX.Element {
  const { data } = props;
  return (
    <Table dataSource={data} pagination={false}>
      <Table.Column title="Semestr" dataIndex="term" key="term" />
      <Table.Column title="Kod kursu" dataIndex="code" key="code" />
      <Table.Column title="Nazwa kursu" dataIndex="name" key="name" />
    </Table>
  );
}

export default TermCodeNameTable;
