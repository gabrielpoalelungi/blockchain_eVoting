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
import { useForm } from "react-hook-form";
import * as yup from 'yup';
import {yupResolver} from "@hookform/resolvers/yup";
import Axios from "axios";
import { useNavigate } from "react-router-dom";

const theme = createTheme();

export default function SignUp() {
  const navigate = useNavigate();

  const schemaValidation = yup.object().shape({
      firstName: yup.string().required("*first name required"),
      lastName: yup.string().required("*last name required"),
      email: yup.string().email("*Invalid email").required("*email required"),
      phoneNumber: yup.string().required("*phone number required")
        .matches(/^(07)[2-9]{1}[0-9]{7}$/, "Phone number must be in the format 07XXXXXXXX"),
      cnp: yup.string().required("*cnp required")
        .matches(/^[1-8]\d{12}$/, "CNP must be a 13-digit number starting with a digit between 1-8"),
        idCardNumber: yup.string().required("*Serial Number required")
        .matches(/^[A-Z]{2}\d{6}$/, 'Serial number must be in the format XX000000, where X is an uppercase letter and 0 is a digit'),
      expirationDate: yup.string().required("*expiration date required"),
      password: yup.string().min(8).max(16).required("*password required"),
      confirmPassword: yup.string().oneOf([yup.ref("password"), null], "*passwords do not match").required()
  })

  const {register, handleSubmit, formState: {errors}} = useForm({
      resolver: yupResolver(schemaValidation)
  });

  const doRegister = (data) => {
    Axios
        .post("http://localhost:8080/auth/register", data)
        .then((response) => {
            navigate("/");
            alert("Registered successfully!");
        })
        .catch((error) => {
            alert("Error " + error.response.status + ": " + error.response.data.message)
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
            Sign up
          </Typography>
          <Box component="form" noValidate onSubmit={handleSubmit(doRegister)} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  autoFocus
                  error={errors.firstName?.message}
                  helperText={errors.firstName?.message}
                  {...register("firstName")}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  error={errors.lastName?.message}
                  helperText={errors.lastName?.message}
                  {...register("lastName")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  error={errors.email?.message}
                  helperText={errors.email?.message}
                  {...register("email")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="phoneNumber"
                  label="Phone Number"
                  name="phoneNumber"
                  type="tel"

                  error={errors.phoneNumber?.message}
                  helperText={errors.phoneNumber?.message}
                  {...register("phoneNumber")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="CNP"
                  label="CNP"
                  name="cnp"

                  error={errors.cnp?.message}
                  helperText={errors.cnp?.message}
                  {...register("cnp")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="idCardNumber"
                  label="Serial Number"
                  name="idCardNumber"

                  error={errors.idCardNumber?.message}
                  helperText={errors.idCardNumber?.message}
                  {...register("idCardNumber")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="expirationDate"
                  label="Expiration Date"
                  name="expirationDate"
                  type="date"
                  InputLabelProps={{shrink: true}}

                  error={errors.expirationDate?.message}
                  helperText={errors.expirationDate?.message}
                  {...register("expirationDate")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
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
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="confirmPassword"
                  label="Confirm Password"
                  type="password"
                  id="confirmPassword"

                  error={errors.confirmPassword?.message}
                  helperText={errors.confirmPassword?.message}
                  {...register("confirmPassword")}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item xs>
                <Link href="#" variant="body2">
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}