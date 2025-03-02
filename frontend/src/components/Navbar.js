// eslint-disable-next-line no-unused-vars
import React, {useState} from 'react'
import {Link} from 'react-router-dom'

function Navbar() {
  return (
    <>
      <nav className="navbar">
        <div className="navbar-container">
            <Link to="/" className="navbar-logo">
                TestNavBaric(cakijevcmaric) <i className="fa-solid fa-flag" />
            </Link>
        </div>
      </nav>
    </>
  )
}

export default Navbar
