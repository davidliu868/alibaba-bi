import React from 'react'
import { Chart, Geom, Axis, Tooltip } from 'bizcharts'

import Container from './Container'
// import styles from './ContainerStyle.css';

class GradeColumn extends React.Component {
  render() {
    const data = [
      {
        grade: '低档',
        price: 38
      },
      {
        grade: '中档',
        price: 100
      },
      {
        grade: '高档',
        price: 200
      }
    ]
    const cols = {
      sales: {
        tickInterval: 40,
        alias: '标题名称'
      }
    }

    return (
      <Container title='浏览'>
        <Chart padding={[20, 20, 30, 40]} height={200} data={data} scale={cols} forceFit='true'>
          <Axis name='grade' />
          <Axis name='price' />
          <Tooltip
            crosshairs={{
              type: 'y'
            }}
          />{' '}
          <Geom type='interval' position='grade*price' />
        </Chart>
      </Container>
    )
  }
}

GradeColumn.propTypes = {}

export default GradeColumn
