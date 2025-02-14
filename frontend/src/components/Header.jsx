import { jwtDecode } from 'jwt-decode'
import React, { useContext, useEffect, useState } from 'react'
import { getDecodedJwt } from '../util.js/jwtUtils'
import "../css/header.css"
import { Link } from 'react-router-dom'
import { UserContext } from '../Context/UserContext'

const Header = () => {
    const userInfo = useContext(UserContext)

    return (
        <header className='header-container'>
            <Link to={"/"}>
                <h1>Unique Name</h1>
            </Link>
            
            
            <div className='header-right-container'>
                <Link to={"/create-ad"}>Create Ad</Link>
                
                <Link to={"/chats"} className='header-chat-container'>
                    <img src="/chat.png" alt="" />
                    Chats
                </Link>

                
                
                <div className='header-profile-container'>
                    <img className='header-profile-pic' src="/user.png" alt="" />
                    <div className='header-username'>
                        {userInfo.username}
                    </div>
                </div> 
            </div>
        </header>
    )
}

export default Header