import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import CodeTab from "./CodeTab";
import { layoutMap } from "@/lib/utils";
import React, { useState } from "react";
import { Badge } from "@/components/ui/badge";
import "./markdown.scss";

function Imprint() {
  return (
      <div className="flex justify-center w-full">
        <div className="px-10 2xl:max-w-[1200px] pt-5 mb-10 w-full relative max-2xl:px-7 max-2xl:mb-0">
          <div className="mt-10 max-xl:mt-4 markdown">
            <h2>Imprint</h2>
            <p>
              TU Dortmund University<br/>
              August-Schmidt-Straße 4<br/>
              44227 Dortmund<br/>
              Germany<br/>
              <br/>
              Tel: +49 234 7551<br/>
              <br/>
              <a href={`mailto:presse@tu-dortmund.de`}>Send e-mail</a>
            </p>
            <p>
              TU Dortmund University, in accordance with section 2 (1) sentence 1 in conjunction with section 1 (2) sentence 1 no. 5 of the Higher Education Act of the State of North Rhine-Westphalia (<i>HG NRW</i>), is a corporation under public law with legal capacity. It is supported by the State of North Rhine-Westphalia and represented by the President, Prof. Dr. Manfred Bayer.
            </p>
            <h4>Value Added Tax Identification Number (VAT ID)</h4>
            <p>
              Value Added Tax Identification Number pursuant to section 27a of the German Value Added Tax Act (UStG): DE 811 258 273</p>
            <p>
              Reservation according to Section 44b (3) Act on Copyright and Related Rights (Urhebergesetz, UrhG) The use and reproduction of a work from the GEMA database contained in our online offer for text and data mining and thus in particular for the training of artificial intelligence is expressly reserved in accordance with Section 44b UrhG. For any intended use for text and data mining purposes, please contact GEMA, Munich.
            </p>
            <h4>Responsibility for content pursuant to section 18 (2) State Media Treaty (Medienstaatsvertrag, MStV):</h4>
            <p>
              Prof. Dr. Falk Howar<br/>
              AQUA Group<br/>
              Chair 14 for Software Engineering<br/>
              Department of Computer Science<br/>
              TU Dortmund University<br/>
              Otto-Hahn-Straße 12<br/>
              44227 Dortmund<br/>
              Germany<br/>
              <br/>
              <a href={`mailto:sekretariat.ls14.fk04@tu-dortmund.de`}>Office management e-mail</a>
            </p>
          </div>
        </div>
      </div>
  );
}

export default Imprint;
