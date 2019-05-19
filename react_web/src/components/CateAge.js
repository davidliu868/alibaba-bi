import React from "react";
import {
    Chart,
    Geom,
    Axis,
    Tooltip,
    Coord,
    Label
} from "bizcharts";
import DataSet from "@antv/data-set";

export default class CateAge extends React.Component {
    render() {
        console.log("Cate Age props:",this.props)
        const { DataView } = DataSet;
        const {ages} = this.props
        const dv = new DataView();
        dv.source(ages).transform({
            type: "percent",
            field: "count",
            dimension: "age",
            as: "percent"
        });
        const cols = {
            percent: {
                formatter: val => {
                    val = (val * 100).toFixed(2) + "%";
                    return val;
                }
            }
        };
        return (
            <Chart
                height={300}
                data={dv}
                scale={cols}
                padding={[20, 10, 20, 10]}
                forceFit
            >
                <Coord type="theta" radius={0.75} />
                <Axis name="percent" />
                <Tooltip
                    showTitle={false}
                    itemTpl="<li><span style=&quot;background-color:{color};&quot; class=&quot;g2-tooltip-marker&quot;></span>{name}: {value}</li>"
                />
                <Geom
                    type="intervalStack"
                    position="percent"
                    color="age"
                    tooltip={[
                        "age*percent",
                        (age, percent) => {
                            percent = percent * 100 + "%";
                            return {
                                name: age,
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
                        formatter={(val, age) => {
                            return age.point.age + ": " + val;
                        }}
                    />
                </Geom>
            </Chart>
        );
    }
}
