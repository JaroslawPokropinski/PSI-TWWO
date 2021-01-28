/* eslint-disable @typescript-eslint/no-explicit-any */
import React from 'react';
import { Table } from 'antd';

function PlanTable(props: { data: any[] }): JSX.Element {
  const { data } = props;
  return (
    <Table
      dataSource={data}
      pagination={false}
      summary={(data1) => {
        let totalW = 0;
        let totalC = 0;
        let totalL = 0;
        let totalP = 0;
        let totalS = 0;
        let totalZZU = 0;
        let totalCNPS = 0;
        let totalECTS = 0;

        data1.forEach(({ w, c, l, p, s, zzu, cnps, ects }) => {
          totalW += w ?? 0;
          totalC += c ?? 0;
          totalL += l ?? 0;
          totalP += p ?? 0;
          totalS += s ?? 0;
          totalZZU += zzu ?? 0;
          totalCNPS += cnps ?? 0;
          totalECTS += ects ?? 0;
        });

        return (
          <>
            <Table.Summary.Row>
              <Table.Summary.Cell index={1} colSpan={3}>
                Razem
              </Table.Summary.Cell>
              <Table.Summary.Cell index={2}>{totalW}</Table.Summary.Cell>
              <Table.Summary.Cell index={3}>{totalC}</Table.Summary.Cell>
              <Table.Summary.Cell index={4}>{totalL}</Table.Summary.Cell>
              <Table.Summary.Cell index={5}>{totalP}</Table.Summary.Cell>
              <Table.Summary.Cell index={6}>{totalS}</Table.Summary.Cell>
              <Table.Summary.Cell index={7}>{totalZZU}</Table.Summary.Cell>
              <Table.Summary.Cell index={8}>{totalCNPS}</Table.Summary.Cell>
              <Table.Summary.Cell index={8}>{totalECTS}</Table.Summary.Cell>
            </Table.Summary.Row>
          </>
        );
      }}
    >
      <Table.Column title="Lp." dataIndex="number" key="number" />
      <Table.Column
        title="Kod kursu/grupy kursów"
        dataIndex="code"
        key="code"
      />
      <Table.Column
        title="Nazwa kursu/grupy kursów"
        dataIndex="name"
        key="name"
      />
      <Table.ColumnGroup title="Tygodniowa liczba godzin">
        <Table.Column title="w" dataIndex="w" key="w" />
        <Table.Column title="ć" dataIndex="c" key="c" />
        <Table.Column title="l" dataIndex="l" key="l" />
        <Table.Column title="p" dataIndex="p" key="p" />
        <Table.Column title="s" dataIndex="s" key="s" />
      </Table.ColumnGroup>
      <Table.Column title="Liczba godzin ZZU" dataIndex="zzu" key="zzu" />
      <Table.Column title="Liczba godzin CNPS" dataIndex="cnps" key="cnps" />
      <Table.Column title="Liczba punktów ECTS" dataIndex="ects" key="ects" />
      <Table.Column
        title="Forma zaliczenia"
        dataIndex="creditioningForm"
        key="creditioningForm"
      />
    </Table>
  );
}

export default PlanTable;
