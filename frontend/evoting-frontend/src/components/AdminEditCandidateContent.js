import * as React from 'react';

import { DataGrid, gridClasses } from '@mui/x-data-grid';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { useQuery } from '@tanstack/react-query';
import Axios from "axios";
import { useNavigate } from "react-router-dom";

export default function AdminEditCandidateContent() {
  const navigate = useNavigate();
  const [candidateList, setCandidateList] = React.useState([])

  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'officialName', headerName: 'Official Name', width: 130 },
    { field: 'description', headerName: 'Description', width: 700},
    {
      field: 'actions',
      headerName: 'Actions',
      width: 220,
      renderCell: (params) => (
        <>
          <div>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="info"
              sx={{ mt: 3, mb: 2 }}
              onClick={() => handleEdit(params.row.id)}
            >
              Edit
            </Button>
          </div>
          <div>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="error"
              sx={{ mt: 3, mb: 2 }}
              onClick={() => handleDelete(params.row.id)}
            >
              Delete
            </Button>
          </div>
        </>
      ),
    },
    
  ];

  useQuery(["getCandidates2Query"], () => {
		const token = `Bearer ${localStorage.getItem("jwt_token")}`
		if (localStorage.getItem("jwt_token") !== null) {
			return Axios
				.get(
						"http://localhost:8080/candidates",
						{ headers: {
								"Authorization" : token
						}}
				)
				.then((response) => setCandidateList(response.data))
				.catch((error) => {
					if (error.response.status === 403) {
						alert("Session has expired. Please log in again!");
						navigate("/signin");
					}
			})
		}
		return null
	})
  
  const handleDelete = (candidateId) => {
    const token = `Bearer ${localStorage.getItem("jwt_token")}`
		if (localStorage.getItem("jwt_token") !== null) {
			return Axios
				.delete(
						`http://localhost:8080/candidates/${candidateId}`,
						{ headers: {
								"Authorization" : token
						}}
				)
				.then((response) => {
          alert("Candidate deleted successfully!")
          navigate("/admin")
        })
				.catch((error) => {
					if (error.response.status === 403) {
						alert("Session has expired. Please log in again!");
						navigate("/signin");
					} else {
            alert("Error " + error.response.status + ": " + error.response.data);
          }
			})
		}
  };

  const handleEdit = (rowId) => {
    navigate(`/admin/editcandidateform/${rowId}`);
  };

  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
      <Box sx={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={candidateList}
        columns={columns}
        getRowHeight={() => 'auto'}
        sx={{
          [`& .${gridClasses.cell}`]: {
            py: 1,
          },
        }}
      />
      </Box>
    </Paper>
  );
}
