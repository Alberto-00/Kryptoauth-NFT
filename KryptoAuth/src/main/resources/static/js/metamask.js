import {openPopupErrorMetamask} from './popup.js'

window.userWalletAddress = null

export function sendAddressToBackend() {
    if (typeof window.ethereum === 'undefined') {
        openPopupErrorMetamask();
        return "openPopup";
    }
    else if (!activeMetaMask())
        return "noAccount"
    return "ok"
}

async function activeMetaMask() {
    const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' }).catch((e) => {
        return false
    })
    if (!accounts) {return false}

    window.userWalletAddress = accounts[0]
    const addressField = document.querySelector("input[name='userAddress']")
    addressField.value = window.userWalletAddress
    return true;
}