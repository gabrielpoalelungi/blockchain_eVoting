import * as React from 'react';

import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import { Link } from 'react-router-dom';

export default function WhatAreWeVotingForContent() {
  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
      <Typography sx={{ my: 5, mx: 2 }} color="text.secondary" align="center" fontSize={"200%"}>
        What are we voting for?
      </Typography>

      <Typography sx={{ my: 0, mx: 2, }} color="text.secondary" align="left" fontSize={"110%"} >
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec velit metus, mollis et sapien non, malesuada pretium mauris. Aliquam tempor leo nisi, nec auctor enim tincidunt in. Aliquam tempor orci nisi, non vestibulum felis fringilla sed. Integer nibh diam, rhoncus a elementum id, auctor consequat diam. Aenean malesuada, ante et tincidunt malesuada, sapien elit bibendum ipsum, nec vehicula orci orci sed ante. Etiam quis quam rutrum, hendrerit lacus id, volutpat nunc. Phasellus rutrum hendrerit sagittis. Maecenas et arcu justo. Morbi elementum felis eu facilisis accumsan
      </Typography>
    </Paper>
  );
}
