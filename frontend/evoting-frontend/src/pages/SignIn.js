import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import jwt from 'jwt-decode'
import {login} from '../pages/store.js'
import { useDispatch } from "react-redux";
import Axios from "axios";
import { useForm } from "react-hook-form";
import * as yup from 'yup';
import {yupResolver} from "@hookform/resolvers/yup";
import { useNavigate } from "react-router-dom";


const theme = createTheme();

export default function SignIn() {
	const dispatch = useDispatch();
  const navigate = useNavigate();

  const schemaValidation = yup.object().shape({
      email: yup.string().email("*Invalid email").required("*email required"),
      password: yup.string().min(8).max(16).required("*password required"),
  })

  const {register, handleSubmit, formState: {errors}} = useForm({
      resolver: yupResolver(schemaValidation)
  });

  const doLogin = (data) => {
    Axios
        .post("http://localhost:8080/auth/login", data)
        .then((response) => {
            const token = response.data.token
            const user = jwt(token)
            
            localStorage.setItem('jwt_token', token);

            dispatch(login({
                username: user.sub,
                isLogged: true,
                role: jwt(token).role
            }))

            navigate("/");
            alert("Logged in successfully!");
        })
        .catch((error) => {
            alert("Bad credentials")
        })
  }

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit(doLogin)} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              error={errors.email?.message}
              helperText={errors.email?.message}
              autoFocus        
              {...register("email")}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"

              error={errors.password?.message}
              helperText={errors.password?.message}
              {...register("password")}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="signup" variant="body2">
									{"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}