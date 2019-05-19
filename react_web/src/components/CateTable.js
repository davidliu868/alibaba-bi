import React, { Component } from 'react'
import Container from './Container'
import { Table, DatePicker } from 'antd'
import { connect } from 'dva'

@connect(({ cateTable }) => cateTable)
class CateTable extends Component {

    constructor(props) {
        super(props)
        this.state = {
            date: '2017-04-22'
        }
    }

    columns = [{
        title: '类目ID',
        dataIndex: 'cateId',
        key: 'cateId'
    }, {
        title: '浏览次数',
        dataIndex: 'nums',
        key: 'nums',
    }, {
        title: '广告收入',
        dataIndex: 'nums',
        key: 'nums2',
    },];

    componentDidMount() {
        // console.log("componentDidMount", this.props)
        const { dispatch } = this.props
        const { date } = this.state
        dispatch({
            type: 'cateTable/fetchCates',
            payload: {
                date: date
            }
        })
    }

    onChange = (date, dateString) => {
        // console.log(date, dateString);
        const {dispatch} = this.props
        dispatch({
            type: 'cateTable/fetchCates',
            payload: {
                date: dateString
            }
        })
    }

    render() {

        // console.log("render props", this.props)
        const { cates } = this.props

        return (
            <Container title='商品类目点击排名'>
                <DatePicker onChange={this.onChange} />
                <div>
                    <Table rowKey={record => record.id} size='small' pagination={false} bordered columns={this.columns} dataSource={cates} />
                </div>
            </Container>
        )
    }
}

export default CateTable