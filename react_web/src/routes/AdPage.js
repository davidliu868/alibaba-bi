
import React from 'react';
import { connect } from 'dva';
import { Layout } from 'antd';

import GradeColumn from '../components/GradeColumn'
import CateCurved from '../components/CateCurved'
import BrandAgeDistribution from '../components/BrandAgeDistribution'
import CateTable from '../components/CateTable'
import CateFunnel from '../components/CateFunnel'
import CateRetain from '../components/CateRetain'
import CateAgeGender from '../components/CateAgeGender'

import style from './AdPage.css'
import "antd/dist/antd.css";

const { Content } = Layout;

const AdPage = ({ dispatch, adDatas }) => {
    return (
        <div>
            {/* title */}
            <div>
                <span className={style.title}>广告商务智能分析系统</span>
            </div>
            {/* 第一页 */}
            <div>
                {/* 左 */}
                <div style={{ width: '25%', float: 'left' }}>
                    <Content className={style.container}>
                        <GradeColumn />
                        <BrandAgeDistribution />
                    </Content>
                </div>
                {/* 中 */}
                <div style={{ width: '50%', float: 'left' }}>
                    <Content className={style.container}>
                        <CateCurved />
                    </Content>
                </div>
                {/* 右 */}
                <div style={{ width: '25%', float: 'left' }}>
                    <Content className={style.container}>
                        <CateTable />
                    </Content>
                </div>
                <div style={{ float: 'left', clear: 'both' }}></div>
            </div>

            {/* 第二页 */}
            <div>
                {/* 左 */}
                <div style={{ width: '40%', float: 'left' }}>
                    <Content className={style.container}>
                        <CateFunnel />
                    </Content>
                </div>
                {/* 右 */}
                <div style={{ width: '60%', float: 'left' }}>
                    <Content className={style.container}>
                        <CateRetain />
                    </Content>
                </div>
                <div style={{ clear: 'both' }}></div>
            </div>
            <div>
                <div style={{ width: '100%', float: 'left' }}>
                    <Content className={style.container}>
                        <CateAgeGender />
                    </Content>
                </div>
            </div>
        </div>
    );
}

export default connect(({ adDatas }) => ({ adDatas }))(AdPage)