import React from "react";
import {
    Chart,
    Geom,
    Axis,
    Tooltip
} from "bizcharts";
import Container from './Container'
import { connect } from 'dva'


@connect(({cateRetain}) => cateRetain)
export default class CateRetain extends React.Component {

    componentDidMount(){
        const { dispatch } = this.props
        dispatch({
            type: 'cateRetain/fetchRetains',
            payload: {
                row: '2017-04-22-000549'
            }
        })
    }
    render() {
        const {retains} = this.props
        const cols = {
            value: {
                min: 0
            },
            year: {
                range: [0, 1]
            }
        };

        return (
            <Container title='留存分析'>
                <Chart height={350} padding={[30, 50, 40, 60]} data={retains} scale={cols} forceFit>
                    <Axis name="date" />
                    <Axis name="value"
                        label={{
                            formatter: val => `${val}%`
                        }}
                    />
                    <Tooltip
                        crosshairs={{
                            type: "y"
                        }}
                    />
                    <Geom type="line" position="date*value" size={2} />
                    <Geom
                        type="point"
                        position="date*value"
                        size={4}
                        shape={"circle"}
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
