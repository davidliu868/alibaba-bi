import React, { Component } from 'react'

import styles from './ContainerStyle.css'

export default class extends Component {
    static propTypes = {
    }

    render() {
        const {title} = this.props;
        return (
            <div className={styles.container}>
                <span className={styles.title}>{title}</span>
                {this.props.children}
            </div>
        )
    }
}
