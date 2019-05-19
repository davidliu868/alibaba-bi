// 不同类目 年龄、性别、消费档次分布

import React, { Component } from 'react'
import { Select } from 'antd'
import Container from './Container'
import CateAge from './CateAge'
import { connect } from 'dva'

const { Option } = Select;

@connect(({cateDistributed}) => cateDistributed)
export default class extends Component {
    constructor(props){
        super(props)
        this.state = {

        }
    }

    componentDidMount(){
        const {dispatch} = this.props

        dispatch({
            type: 'cateDistributed/fetchAges',
            payload:{
                cateId: 4284
            }
        })

        dispatch({
            type: 'cateDistributed/fetchGenders',
            payload:{
                cateId: 4284
            }
        })

        dispatch({
            type: 'cateDistributed/fetchPvalues',
            payload:{
                cateId: 4284
            }
        })
    }

    handleChange = (cateId)=> {
        const {dispatch} = this.props
        dispatch({
            type: 'cateDistributed/fetchAges',
            payload:{
                cateId: cateId
            }
        })

        dispatch({
            type: 'cateDistributed/fetchGenders',
            payload:{
                cateId: cateId
            }
        })

        dispatch({
            type: 'cateDistributed/fetchPvalues',
            payload:{
                cateId: cateId
            }
        })
    }

    render() {

        const {ages, pvalues, genders} = this.props

        return (
            <Container title='年龄、性别、消费档次分布'>
                <div>
                    <Select defaultValue="0" style={{ width: 120, marginLeft: 10, marginTop: 10 }} onChange={this.handleChange}>
                        <Option value="0">选择类目</Option>
                        <Option value="6427">6427</Option>
                        <Option value="12154">12154</Option>
                        <Option value="4284">4284</Option>
                        <Option value="4771">4771</Option>
                        <Option value="6510">6510</Option>
                        <Option value="1385">1385</Option>
                        <Option value="9443">9443</Option>
                        <Option value="9095">9095</Option>
                        <Option value="4824">4824</Option>
                        <Option value="392">392</Option>
                        <Option value="8446">8446</Option>
                    </Select>
                </div>
                <div style={{ position: 'relative' }}>
                    <div style={{ width: '30%', float: 'left' }}>
                        <CateAge ages={ages}/>
                        <span style={{ display: 'block', textAlign: 'center', marginBottom: 10, fontSize: '8pt', fontWeight: 'bold' }}>年龄分布</span>
                    </div>
                    <div style={{ width: '30%', float: 'left' }}>
                        <CateAge ages={genders} />
                        <span style={{ display: 'block', textAlign: 'center', marginBottom: 10, fontSize: '8pt', fontWeight: 'bold' }}>性别分布</span>
                    </div>
                    <div style={{ width: '30%', float: 'left' }}>
                        <CateAge ages={pvalues} />
                        <span style={{ display: 'block', textAlign: 'center', marginBottom: 10, fontSize: '8pt', fontWeight: 'bold' }}>消费档次分布</span>
                    </div>
                    <div style={{ clear: 'both' }}></div>
                </div>

            </Container>
        )
    }
}
