import * as React from 'react';

import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Axios from "axios";
import { useForm } from "react-hook-form";
import * as yup from 'yup';
import {yupResolver} from "@hookform/resolvers/yup";
import { useNavigate } from "react-router-dom";
import HowToVoteIcon from '@mui/icons-material/HowToVote';

export default function AdminEditVoteSessionContent() {
  const navigate = useNavigate();

  const schemaValidation = yup.object().shape({
    startingAt: yup.string().required("*starting date required"),
    endingAt: yup.string().required("*ending date required"),
  })

  const {register, handleSubmit, formState: {errors}} = useForm({
    resolver: yupResolver(schemaValidation)
  });

  const doCreateVoteSession = (data) => {
    const token = `Bearer ${localStorage.getItem("jwt_token")}`
    if (localStorage.getItem("jwt_token") !== null) {
      return Axios
        .put(
            "http://localhost:8080/voting-session/update-dates", data,
            { headers: {
                "Authorization" : token
            }}
        )
        .then((response) => {
          alert("Voting session updated successfully!");
          navigate("/admin")
        })
        .catch((error) => {
          if (error.response.status === 403) {
            alert("Session has expired. Please log in again!");
            navigate("/signin");
          } else {
            alert("Error " + error.response.status + ": " + error.response.data);
            navigate("/admin")
          }
      })
    }
  }

  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
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
            <HowToVoteIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Update Vote Session
          </Typography>
          <Box component="form" noValidate onSubmit={handleSubmit(doCreateVoteSession)} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="startingAt"
                  label="Starting At"
                  name="startingAt"
                  type="datetime-local"
                  InputLabelProps={{shrink: true}}

                  error={errors.startingAt?.message}
                  helperText={errors.startingAt?.message}
                  {...register("startingAt")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="endingAt"
                  label="Ending At"
                  name="endingAt"
                  type="datetime-local"
                  InputLabelProps={{shrink: true}}

                  error={errors.endingAt?.message}
                  helperText={errors.endingAt?.message}
                  {...register("endingAt")}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Update Vote Session
            </Button>
          </Box>
        </Box>
      </Container>
    </Paper>
  );
}
