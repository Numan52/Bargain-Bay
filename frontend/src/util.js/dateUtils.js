export function formatDateTime(dateTime) {
    let date = new Date(dateTime)
    let now = new Date(Date.now())


    if (isSameDay(date, now)) {
        return date.toLocaleTimeString("en-GB", {hour:"2-digit", minute:"2-digit"})
    } else {
        return date.toLocaleDateString()
    }
}


export function DateToTime(dateTime) {
    return new Date(dateTime).toLocaleTimeString("en-GB", {hour:"2-digit", minute:"2-digit"})
}


export function isSameDay(dateOne, dateTwo) {
    return (
        dateOne.getFullYear() === dateTwo.getFullYear() && 
        dateOne.getMonth() === dateTwo.getMonth() && 
        dateOne.getDate() === dateTwo.getDate()
    )
}