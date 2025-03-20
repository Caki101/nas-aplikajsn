import React from 'react'
import ContentHeader from './ContentHeader'
import s from '../styles/Content.module.css';

const Content = () => {
  return (
    <div className={s["content"]}>
      <ContentHeader />
    </div>
  )
}

export default Content