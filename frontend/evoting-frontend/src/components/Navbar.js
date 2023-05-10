import React from "react";
import {
  NavbarContainer,
  LeftContainer,
  RightContainer,
  NavbarInnerContainer,
  NavbarLinkContainer,
  NavbarLink
} from "../styles/NavigationBar.style";


function NavigationBar() {

  return (
    <NavbarContainer>
      <NavbarInnerContainer>
        <LeftContainer>
          <NavbarLinkContainer>
            <NavbarLink to="/"> Home</NavbarLink>
          </NavbarLinkContainer>
        </LeftContainer>
        <RightContainer>
          <h1>E-voting Platform</h1>
        </RightContainer>
      </NavbarInnerContainer>
    </NavbarContainer>
  );
}

export default NavigationBar;