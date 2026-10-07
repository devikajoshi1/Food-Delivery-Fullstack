export const rupees = new Intl.NumberFormat('en-IN',{
    style:'currency',
    currency:'INR',
})

export function statusText(status){
    const words = status.toLowerCase().replaceAll('_',' ')
    return words[0].toUpperCase()+ words.slice(1)
}