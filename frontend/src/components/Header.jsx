import { jwtDecode } from 'jwt-decode'
import React, { useContext, useEffect, useState } from 'react'
import { getDecodedJwt } from '../util.js/jwtUtils'
import "../css/header.css"
import { Link, useNavigate } from 'react-router-dom'
import { UserContext } from '../Context/UserContext'


const Header = () => {
    let userInfo = useContext(UserContext)
    const navigate = useNavigate()



    return (
        <header className='header-container'>
            <Link to={"/"}>
                <h1>Bargain-Bay</h1>
            </Link>
            
            
            <div className='header-right-container'>
                <Link to={"/create-ad"} className='header-create-ad-container'>
                    <img src="/plus.png" alt="" />
                    Create Ad
                </Link>
                
                <Link to={"/chats"} className='header-chat-container'>
                    <img src="/chat.png" alt="" />
                    Chats
                </Link>

                {userInfo.userId &&
                    <div className='header-profile-container'>
                        <img className='header-profile-pic' src="/user.png" alt="" />
                        <div className='header-username'>
                            {userInfo.username}
                        </div>
                    </div> 
                }

                {!userInfo.userId &&
                    <button className='login-button' onClick={() => navigate("/login")}>
                        Login
                    </button>
                }

                {userInfo.userId &&
                    <button className='login-button' onClick={() => {
                        localStorage.removeItem("token")
                        location.reload()
                    }}>
                        Logout
                    </button>                                                                                                                                                                                                                                                                                                                                                                                        
                }
                
                
            </div>
        </header>
    )
}

export default Header