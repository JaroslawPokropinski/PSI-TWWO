import React from 'react';
import { Table } from 'antd';

function EctsDeficitTable(props: { data: any[] }): JSX.Element {
  const { data } = props;
  return (
    <Table dataSource={data} pagination={false}>
      <Table.Column title="Semestr" dataIndex="term" key="term" />
      <Table.Column
        title="Dopuszczalny deficyt punktów po semestrze"
        dataIndex="deficit"
        key="deficit"
      />
    </Table>
  );
}

export default EctsDeficitTable;
