import { getJwt } from "../jwtUtils"

const apiUrl = import.meta.env.VITE_API_URL

async function makeApiRequest({ 
    endpoint, 
    method = "GET", 
    body = null, 
    requiresAuth = false, 
    customHeaders = {} 
}) {
    if (requiresAuth) {
        const token = getJwt()
        customHeaders["Authorization"] = `Bearer ${token}`
    }

    if (body && !(body instanceof FormData) && !customHeaders["Content-Type"]) {
        customHeaders["Content-Type"] = "application/json"
    }

    try {
        const response = await fetch(`${apiUrl}${endpoint}`, {
            method,
            headers: customHeaders,
            body
        })
    
        const data = await response.json().catch(() => null)
        console.log("data", data)
        if (!response.ok) {
            const message = data?.error || "An unexpected error occurred."
            console.log(data)
            throw new Error(message)
        }
    
        return data
    } catch (error) {
        if (error instanceof TypeError) {
            throw new Error("An unexpected Error occurred.")
        }
        throw error
    }
    
} 


function loginUser(username, password) {
    return makeApiRequest({
        endpoint: "/login",
        method: "POST",
        body: JSON.stringify({username, password})
    })
}


function registerUser(firstName, lastName, username, password, email) {
    return makeApiRequest({
        endpoint: "/register",
        method: "POST",
        body: JSON.stringify({firstName, lastName, email, username, password})
    })
}


function postAd(form) {
    return makeApiRequest({
        endpoint: "/ads",
        method: "POST",
        body: form,
        requiresAuth: true
    })
}


function getAds(offset, limit) {
    return makeApiRequest({
        endpoint: `/ads?offset=${offset}&limit=${limit}`,
        method: "GET",
    })
}


function getAd(id) {
    return makeApiRequest({
        endpoint: `/ad?id=${id}`,
        method: "GET",
    })
}


function getUser(id) {
    return makeApiRequest({
        endpoint: `/user?id=${id}`,
        method: "GET"
    })
}


function getUserId(username) {
    return makeApiRequest({
        endpoint: `/user/id?username=${username}`,
        method: "GET"
    })
}


function getAllContacts() {
    return makeApiRequest({
        endpoint: `/chats/contacts`,
        method: "GET",
        requiresAuth: true
    })
}


function getChatMessages(chatId) {
    return makeApiRequest({
        endpoint: `/chats/${chatId}/messages`,
        method: "GET",
        requiresAuth: true
    })
}


function markChatAsSeen(chatId) {
    return makeApiRequest({
        endpoint: `/chats/${chatId}/seen`,
        method: "POST",
        requiresAuth: true
    })
}



export {makeApiRequest, loginUser, registerUser, postAd, getAds, getAd, getUser, getUserId, getAllContacts, getChatMessages, markChatAsSeen}
    


