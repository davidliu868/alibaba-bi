import React from "react";
import {
    Chart,
    Geom,
    Tooltip,
    Coord,
    Label,
    Legend,
    Guide,
} from "bizcharts";
import DataSet from "@antv/data-set";
import Container from '../components/Container'
import { DatePicker, Select, message } from 'antd'
import { connect } from 'dva'

const { Option } = Select;

@connect(({ cateFunnel }) => cateFunnel)
export default class CateFunnel extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            date: '',
            id: 0,
        }
    }

    componentDidMount() {
        const { dispatch } = this.props

        dispatch({
            type: 'cateFunnel/fetchFunnels',
            payload: {
                date: '2017-04-22',
                id: 4609,
            }
        })

        dispatch({
            type: 'cateFunnel/fetchCateIds',
        })
    }

    /**
     * datePicker change
     */
    onDateChange = (date, dateString) => {
        this.setState({
            date: dateString
        })
    }

    /**
     * 下拉选择
     * @param {*} value 
     */
    handleChange = (value) => {
        
        const { dispatch } = this.props
        const { date} = this.state


        if (date === '') {
            message.info("日期不能为空")
            return
        }
        dispatch({
            type: 'cateFunnel/fetchFunnels',
            payload: {
                date: date,
                id: value,
            }
        })
    }

    render() {
        const { Text } = Guide;
        const { DataView } = DataSet;
        const { funnels, cateIds } = this.props
        let data = funnels
        const dv = new DataView().source(data);
        dv.transform({
            type: "percent",
            field: "pv",
            dimension: "action",
            as: "percent"
        });
        data = dv.rows;
        const cols = {
            percent: {
                nice: false
            }
        };

        return (
            <Container title='漏斗分析'>
                <div style={{ marginLeft: '20px', marginTop: '20px' }}>
                    <DatePicker onChange={this.onDateChange} />
                    <Select defaultValue="0" style={{ width: 120, marginLeft: 10 }} onChange={this.handleChange}>
                        <Option value="0">选择类目</Option>
                        {
                            cateIds.map((v, index) => {
                                return <Option key={index} value={v}> {v} </Option>
                            })
                        }
                    </Select>
                </div>

                <Chart
                    height={300}
                    data={data}
                    scale={cols}
                    padding={[40, 160, 60, 40]}
                    forceFit
                >
                    <Tooltip
                        showTitle={false}
                        itemTpl="<li data-index={index} style=&quot;margin-bottom:4px;&quot;><span style=&quot;background-color:{color};&quot; class=&quot;g2-tooltip-marker&quot;></span>{name}<br/><span style=&quot;padding-left: 16px&quot;>浏览人数：{pv}</span><br/><span style=&quot;padding-left: 16px&quot;>占比：{percent}</span><br/></li>"
                    />
                    <Coord type="rect" transpose scale={[1, -1]} />
                    <Legend />
                    <Guide>
                        {data.map(obj => {
                            return (
                                <Text
                                    key={obj.id}
                                    top={true}
                                    position={{
                                        action: obj.action,
                                        percent: "median"
                                    }}
                                    content={parseInt(obj.percent * 100) + "%"}
                                    style={{
                                        fill: "#fff",
                                        fontSize: "12",
                                        textAlign: "center",
                                        shadowBlur: 2,
                                        shadowColor: "rgba(0, 0, 0, .45)"
                                    }}
                                />
                            );
                        })}
                    </Guide>
                    <Geom
                        type="intervalSymmetric"
                        position="action*percent"
                        shape="funnel"
                        color={[
                            "action",
                            ["#0050B3", "#1890FF", "#40A9FF", "#69C0FF", "#BAE7FF"]
                        ]}
                        tooltip={[
                            "action*pv*percent",
                            (action, pv, percent) => {
                                return {
                                    name: action,
                                    percent: parseInt(percent * 100) + "%",
                                    pv: pv
                                };
                            }
                        ]}
                    >
                        <Label
                            content={[
                                "action*pv",
                                (action, pv) => {
                                    return action + " " + pv;
                                }
                            ]}
                            offset={35}
                            labeLine={{
                                lineWidth: 1,
                                stroke: "rgba(0, 0, 0, 0.15)"
                            }}
                        />
                    </Geom>
                </Chart>
            </Container>
        );
    }
}