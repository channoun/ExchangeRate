import './App.css';
import { useState, useEffect } from 'react';

const App = () => {
  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);

  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  let [transactionType, setTransactionType] = useState("usd-to-lbp");

  const getRates = async () => {
    try {
      const response = await fetch("http://127.0.0.1:500/exchangeRate");
      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
      const data = await response.json();
      setBuyUsdRate(data.usd);
      setSellUsdRate(data.lbp);
    } catch (err) {
      console.error(err.message);
    }

  }
  useEffect(() => { getRates(); }, []);

  return (
    <div className="App">
      <h1>Exchange Rate</h1>
      <h2>Today's exchange Rate</h2>
      <p>
        Buy USD: <span id="buy-usd-rate">{buyUsdRate}</span>
        Sell USD: <span id="sell-usd-rate">{sellUsdRate}</span>
      </p>
      <form id="transaction-form">
        <label htmlFor="lbp-amount">Enter LBP amount: </label>
        <input type="number" id="lbp-amount" value={lbpInput} onChange={e => setLbpInput(e.target.value)} />
        <label htmlFor="usd-amount">Enter USD amount: </label>
        <input type="number" id="usd-amount" value={usdInput} onChange={e => setUsdInput(e.target.value)} />
        <div id="radio-container">
          <label htmlFor="usd-to-lbp">USD to LBP</label>
          <input type="radio" onClick={e => setTransactionType("usd-to-lbp")} id="usd-to-lbp" name="transaction" />
          <label htmlFor="lbp-to-usd">LBP to USD</label>
          <input type="radio" onClick={e => setTransactionType("lbp-to-usd")} id="lbp-to-usd" name="transaction" />
        </div>
        <input type="submit" value="Add Transaction" onClick={e => {
          e.preventDefault(); console.log({
            lbpAmount: parseFloat(lbpInput),
            usdAmount: parseFloat(usdInput),
            transactionType: transactionType === "usd-to-lbp" ? "BUY" : "SELL"
          });
        }} />
      </form>
    </div>
  );
}

export default App;
