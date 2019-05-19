
// 前10类品牌不同年龄层级分布
import React from "react";
import {
    Chart,
    Geom,
    Axis,
    Tooltip,
    Coord,
    Label,
    Legend,
} from "bizcharts";
import DataSet from "@antv/data-set";
import {Select} from 'antd'
import Container from './Container'

const {Option} = Select;

export default class BrandAgeDistribution extends React.Component {
    render() {
        const { DataView } = DataSet;
        const data = [
            {
                level: "等级一",
                count: 50
            },
            {
                level: "等级二",
                count: 50
            },
            {
                level: "等级三",
                count: 50
            },
            {
                level: "等级四",
                count: 50
            }
        ];
        const dv = new DataView();
        dv.source(data).transform({
            type: "percent",
            field: "count",
            dimension: "level",
            as: "percent"
        });
        const cols = {
            percent: {
                formatter: val => {
                    val = val * 100 + "%";
                    return val;
                }
            }
        };

        const handleChange = ()=>{

        }
        return (
            <Container title='不同品牌年龄层级分布'>
                <Select defaultValue="0" style={{ width: 120, marginLeft: 10, marginTop: 5 }} onChange={handleChange}>
                    <Option value="0">选择品牌</Option>
                    <Option value="1">品牌一</Option>
                    <Option value="2">品牌二</Option>
                    <Option value="3">品牌三</Option>
                </Select>
                <Chart
                    height={200}
                    data={dv}
                    scale={cols}
                    padding={[0, 0, 0, 70]}
                    forceFit
                >
                    <Coord type="theta" radius={0.75} />
                    <Axis name="percent" />
                    <Legend
                        position="left-center"
                    />
                    <Tooltip
                        showTitle={false}
                        itemTpl="<li><span style=&quot;background-color:{color};&quot; class=&quot;g2-tooltip-marker&quot;></span>{name}: {value}</li>"
                    />
                    <Geom
                        type="intervalStack"
                        position="percent"
                        color="level"
                        tooltip={[
                            "level*percent",
                            (level, percent) => {
                                percent = percent * 100 + "%";
                                return {
                                    name: level,
                                    value: percent
                                };
                            }
                        ]}
                        style={{
                            lineWidth: 1,
                            stroke: "#fff"
                        }}
                    >
                        <Label
                            content="percent"
                            offset={-40}
                            textStyle={{
                                rotate: 0,
                                textAlign: "center",
                                shadowBlur: 2,
                                shadowColor: "rgba(0, 0, 0, .45)"
                            }}
                        />
                    </Geom>
                </Chart>
            </Container>
        );
    }
}

