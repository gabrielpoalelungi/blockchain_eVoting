import * as React from 'react';

import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';

export default function AdminEditVoteSessionContent() {
  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
      <Typography sx={{ my: 5, mx: 2 }} color="text.secondary" align="center" fontSize={"200%"}>
        Edit Vote Session!
      </Typography>
    </Paper>
  );
}
