import * as React from 'react';
import PropTypes from 'prop-types';
import AppBar from '@mui/material/AppBar';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import IconButton from '@mui/material/IconButton';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import Button from '@mui/material/Button';
import { logout } from '../pages/store';


function Header() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  let isLogged = useSelector((state) => state.user.value.isLogged);
  let role = useSelector((state) => state.user.value.role);
  let email = useSelector((state) => state.user.value.username);

  const logoutAction = () => {
    dispatch(logout())
    localStorage.setItem('jwt_token', null);
    alert("Logged out!")
    isLogged = false;
    navigate("/")
  }

  return (
    <React.Fragment>
      <AppBar color="primary" position="sticky" elevation={0}>
        <Toolbar>
          <Grid container spacing={1} alignItems="center">
            <Grid item>
              {isLogged && (
                <Button variant="contained" color="secondary" onClick={logoutAction}>
                  Logout
                </Button>
              )}
            </Grid>
            <Grid item xs />
            <Grid item>
              <IconButton color="inherit" sx={{ p: 0.5 }}>
                <Avatar src="/static/images/avatar/1.jpg" alt="My Avatar" />
              </IconButton>
            </Grid>
            <Grid>
              {isLogged && (
                <Typography fontSize={'120%'} marginTop={'4%'}>{email}</Typography>
              )}
              {!isLogged && 
                  <Typography fontSize={'20px'} style={{marginTop: "10%"}}>
                    <Link to="/signin" color="inherit"> Sign in</Link>
                  </Typography>
              }
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
      <AppBar
        component="div"
        color="primary"
        position="static"
        elevation={0}
        sx={{ zIndex: 0 }}
      />
    </React.Fragment>
  );
}

Header.propTypes = {
  onDrawerToggle: PropTypes.func.isRequired,
};

export default Header;
