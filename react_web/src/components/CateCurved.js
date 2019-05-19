import React from "react";
import {
  Chart,
  Geom,
  Axis,
  Tooltip,
  Legend,
} from "bizcharts";
import DataSet from "@antv/data-set";
import { DatePicker, Divider, Select } from 'antd';
import Container from './Container'

import styles from './CateCurved.css'

const { RangePicker } = DatePicker;
const { Option } = Select;

export default class CateCurved extends React.Component {
  render() {
    const data = [
      {
        date: "2019-01-01",
        pv: 7,
        put: 50,
        buy: 10
      },
      {
        date: "2019-01-02",
        pv: 70,
        put: 58,
        buy: 20
      },
      {
        date: "2019-01-03",
        pv: 45,
        put: 30,
        buy: 30
      },
      {
        date: "2019-01-04",
        pv: 99,
        put: 20,
        buy: 40
      },
    ];
    const ds = new DataSet();
    const dv = ds.createView().source(data);
    dv.transform({
      type: "fold",
      fields: ["pv", "put", "buy"],
      // 展开字段集
      key: "type",
      // key字段
      value: "clickTimes" // value字段
    });
    console.log(dv);
    const cols = {
      month: {
        range: [0, 1]
      }
    };

    function onChange(date, dateString) {
      console.log(date, dateString);
    }

    function handleChange(value) {
      console.log(`selected ${value}`);
    }


    return (
      <Container title='商品类目浏览量'>
        <div className={styles.pick_style}>
          <RangePicker onChange={onChange} />
          <Select defaultValue="0" style={{ width: 120, marginLeft: 10 }} onChange={handleChange}>
            <Option value="0">选择类目</Option>
            <Option value="1">类目一</Option>
            <Option value="2">类目二</Option>
            <Option value="3">类目三</Option>
          </Select>
        </div>

        <Divider/>
        <Chart height={300} padding={[20, 60, 70, 50]} data={dv} scale={cols} forceFit>
          <Legend />
          <Axis name="date" />
          <Axis
            name="次数"
            label={{
              formatter: val => `${val}次`
            }}
          />
          <Tooltip
            crosshairs={{ type: 'cross' }}
          />
          <Geom
            type="line"
            position="date*clickTimes"
            size={2}
            color={"type"}
            shape={"smooth"}
          />
          <Geom
            type="point"
            position="date*clickTimes"
            size={4}
            shape={"circle"}
            color={"type"}
            style={{
              stroke: "#fff",
              lineWidth: 1
            }}
          />
        </Chart>
      </Container>
    );
  }
}
