import './App.css';
import { useState, useEffect, useCallback } from 'react';
import { AppBar, Toolbar, Typography, Button, Snackbar, Alert, TextField, Box, InputAdornment, FormControl, FormLabel, FormControlLabel, RadioGroup, Radio } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken, saveUserToken, clearUserToken } from "./localStorage";
import { DataGrid } from "@mui/x-data-grid";

const States = {
  PENDING: "PENDING",
  USER_CREATION: "USER_CREATION",
  USER_LOG_IN: "USER_LOG_IN",
  USER_AUTHENTICATED: "USER_AUTHENTICATED",
};

const dataGridColumns = [
  { field: 'added_date', headerName: 'Added Date', flex: 2 },
  { field: 'lbp_amount', headerName: 'LBP', flex: 1 },
  { field: 'usd_amount', headerName: 'USD', flex: 1 },
  { field: 'usd_to_lbp', headerName: 'USD to LBP', flex: 1 },
];

const SERVER_URL = "http://127.0.0.1:5000";

const App = () => {
  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);

  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  let [transactionType, setTransactionType] = useState("lbp-to-usd");
  let [userToken, setUserToken] = useState(getUserToken());
  let [authState, setAuthState] = useState(States.PENDING);
  let [usdBuy, setUsdBuy] = useState(0);
  let [usdSell, setUsdSell] = useState(0);
  let [userTransactions, setUserTransactions] = useState([]);

  const getRates = async () => {
    try {
      const response = await fetch(`${SERVER_URL}/exchangeRate`);
      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
      const data = await response.json();
      setBuyUsdRate(data["lbp_to_usd"]);
      setSellUsdRate(data["usd_to_lbp"]);
    } catch (err) {
      console.error(err.message);
    }

  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Validate inputs first
    if (lbpInput === "" || usdInput === "") {
      console.error("Missing input fields");
    } else {
      try {
        const response = await fetch(`${SERVER_URL}/transaction`, userToken ? {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${userToken}`
          },
          body: JSON.stringify({
            "usd_amount": usdInput,
            "lbp_amount": lbpInput,
            "usd_to_lbp": transactionType === "usd-to-lbp"
          })
        } : {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            "usd_amount": usdInput,
            "lbp_amount": lbpInput,
            "usd_to_lbp": transactionType === "usd-to-lbp"
          })
        });
        const data = await response.json();
        setLbpInput("");
        setUsdInput("");
        fetchUserTransactions();
      } catch (err) {
        console.error(err.message);
      }
    }
  }

  const handleDialogClose = () => { setAuthState(States.PENDING); }

  const login = async (username, password) => {
    return fetch(`${SERVER_URL}/authentication`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setAuthState(States.USER_AUTHENTICATED);
        setUserToken(body.token);
        saveUserToken(body.token);
      });
  }

  const createUser = async (username, password) => {
    return fetch(`${SERVER_URL}/user`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    }).then((response) => login(username, password));
  }

  const fetchUserTransactions = useCallback(() => {
    fetch(`${SERVER_URL}/transaction`, {
      headers: {
        Authorization: `bearer ${userToken}`,
      },
    })
      .then((response) => response.json())

      .then((transactions) => setUserTransactions(transactions));
  }, [userToken]);

  useEffect(() => {
    if (userToken) {
      fetchUserTransactions();
    }
  }, [fetchUserTransactions, userToken]);

  useEffect(() => { getRates(); }, [lbpInput, usdInput]);

  return (
    <div className="App">
      <AppBar position="static">
        <Toolbar classes={{ root: "nav" }}>
          <Typography variant="h5">Exchange Rate</Typography>
          <div>
            {userToken !== null ? (
              <Button color="inherit" onClick={() => { setUserToken(null); clearUserToken(); }}>
                Logout
              </Button>
            ) : (
              <div>
                <Button
                  color="inherit"
                  onClick={() => setAuthState(States.USER_CREATION)}
                >
                  Register
                </Button>
                <Button
                  color="inherit"
                  onClick={() => setAuthState(States.USER_LOG_IN)}
                >
                  Login
                </Button>
              </div>
            )}
          </div>
        </Toolbar>
      </AppBar>
      <UserCredentialsDialog open={authState === States.USER_CREATION} onClose={handleDialogClose} submitText={"Register"} title={"Create User"} onSubmit={createUser} />
      <UserCredentialsDialog open={authState === States.USER_LOG_IN} onClose={handleDialogClose} submitText={"Login"} title={"Login"} onSubmit={login} />
      <Snackbar
        elevation={6}
        variant="filled"
        open={authState === States.USER_AUTHENTICATED}
        autoHideDuration={2000}
        onClose={() => setAuthState(States.PENDING)}
      >
        <Alert severity="success">Success</Alert>
      </Snackbar>
      <div className="container">
        <div className="wrapper">
          <div className="inner-wrapper">
            <h2>Today's Exchange Rate</h2>
            <p>
              Buy USD: <span id="buy-usd-rate">{buyUsdRate}LBP</span>
              Sell USD: <span id="sell-usd-rate">{parseFloat(sellUsdRate).toFixed(2).toString()}LBP</span>
            </p>
          </div>
          <div className="inner-wrapper">
            <h2>Rate Calculator</h2>
            <div className="calculator">
              <TextField fullWidth label="Buy USD amount" type="number" variant="outlined" value={usdBuy} onChange={(e) => {
                setUsdBuy(e.target.value);
              }} InputProps={{
                endAdornment: (
                  <div style={{ display: 'flex' }}>
                    <InputAdornment position="end">
                      <Box sx={{
                        width: '100%',
                        height: '100%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                      }}>{(usdBuy * buyUsdRate).toFixed(2)} LBP</Box>
                    </InputAdornment>
                  </div>
                ),
              }} />

              <TextField fullWidth label="Sell USD amount" type="number" variant="outlined" value={usdSell} onChange={(e) => {
                setUsdSell(e.target.value);
              }} InputProps={{
                endAdornment: (
                  <div style={{ display: 'flex' }}>
                    <InputAdornment position="end">
                      <Box sx={{
                        width: '100%',
                        height: '100%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                      }}>{(usdSell * sellUsdRate).toFixed(2)} LBP</Box>
                    </InputAdornment>
                  </div>
                ),
              }} />
            </div>
          </div>
        </div>
        <div className="wrapper">
          <form onSubmit={handleSubmit} id="transaction-form">
            <FormControl component="fieldset">
              <h2>New Transaction</h2>
              <TextField
                id="lbp-amount"
                label="LBP Amount"
                value={lbpInput}
                onChange={(e) => setLbpInput(e.target.value)}
                margin="normal"
                type="number"
                fullWidth
              />
              <TextField
                id="usd-amount"
                label="USD Amount"
                value={usdInput}
                onChange={(e) => setUsdInput(e.target.value)}
                margin="normal"
                type="number"
                fullWidth
              />
            </FormControl>
            <FormControl component="fieldset">
              <FormLabel component="legend">Transaction Type</FormLabel>
              <RadioGroup row value={transactionType} onChange={(e) => setTransactionType(e.target.value)}>
                <FormControlLabel value="lbp-to-usd" control={<Radio />} label="Buy USD" />
                <FormControlLabel value="usd-to-lbp" control={<Radio />} label="Sell USD" />
              </RadioGroup>
            </FormControl>
            <Button type="submit" variant="contained" color="primary">
              Add Transaction
            </Button>
          </form>
        </div>
        {userToken && (
          <div className="wrapper">
            <h2>Your Transactions</h2>
            <DataGrid
              rows={userTransactions}
              columns={dataGridColumns}
              pageSizeOptions={[5]} />
          </div>
        )}
      </div>
    </div >
  );
}

export default App;
