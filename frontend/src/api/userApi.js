import { makeApiRequest } from "./api"


function loginUser(username, password) {
    return makeApiRequest({
        endpoint: "/login",
        method: "POST",
        body: JSON.stringify({ username, password })
    })
}


function registerUser(firstName, lastName, username, password, email) {
    return makeApiRequest({
        endpoint: "/register",
        method: "POST",
        body: JSON.stringify({ firstName, lastName, email, username, password })
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


export {loginUser, registerUser, getUser, getUserId};

