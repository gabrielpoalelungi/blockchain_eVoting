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
import HowToRegIcon from '@mui/icons-material/HowToReg';
import { useNavigate } from "react-router-dom";
import { useParams } from 'react-router-dom';

export default function AdminEditCandidateFormContent() {
  const navigate = useNavigate();
  const {candidateId} = useParams();

  const schemaValidation = yup.object().shape({
    officialName: yup.string().required("*official name required"),
    description: yup.string().min(50).required("*description required"),
  })

  const {register, handleSubmit, formState: {errors}} = useForm({
    resolver: yupResolver(schemaValidation)
  });

  const doUpdateCandidate = (data) => {
    const token = `Bearer ${localStorage.getItem("jwt_token")}`
    if (localStorage.getItem("jwt_token") !== null) {
      return Axios
        .put(
            `http://localhost:8080/candidates/${candidateId}`, data,
            { headers: {
                "Authorization" : token
            }}
        )
        .then((response) => {
          alert("Candidate updated successfully!");
          navigate("/admin")
        })
        .catch((error) => {
          if (error.response.status === 403) {
            alert("Session has expired. Please log in again!");
            navigate("/signin");
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
            <HowToRegIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Update Candidate
          </Typography>
          <Box component="form" noValidate onSubmit={handleSubmit(doUpdateCandidate)} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="officialName"
                  label="Official Name"
                  name="officialName"

                  error={errors.officialName?.message}
                  helperText={errors.officialName?.message}
                  {...register("officialName")}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                id="description"
                label="Description"
                multiline
                rows={4}

                error={errors.description?.message}
                helperText={errors.description?.message}
                {...register("description")}

                sx={{width: '100%'}}
              />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Update candidate
            </Button>
          </Box>
        </Box>
      </Container>
    </Paper>
  );
}
