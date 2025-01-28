import { Banner } from "@/components/ui/banner";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
} from "@/components/ui/breadcrumb";
import { Link } from "react-router-dom";

function Packages({ packages }: { packages: any }) {
  return (
    <div className="flex justify-center w-full">
      <div className="px-10 max-w-[1600px] pt-5 mb-10 w-full component">
        <Breadcrumb className="mb-10">
          <BreadcrumbList>
            <BreadcrumbItem>
              <BreadcrumbLink to={`/docs/`}>Reference</BreadcrumbLink>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>
        <div className=" flex flex-col gap-5">
          {Object.keys(packages).map((key) => {
            let elem = packages[key];
            return (
              <Link to={"/docs/" + elem.package} className="group">
                <div className="bg-muted/50 border-none px-4 py-4 rounded-xl flex gap-5 items-center relative docs-banner">
                  <div className="flex items-center gap-5">
                    <i className="material-symbols-rounded text-muted-foreground rounded-lg bg-muted aspect-square w-14 h-14 p-4 flex items-center justify-center">
                      {elem.icon}
                    </i>
                  </div>
                  <div>
                    <h4 className="font-bold text-lg group-hover:translate-x-1  transition-all">
                      {elem.title}
                    </h4>
                    <h1 className="text-base text-muted-foreground  group-hover:translate-x-1 transition-all duration-300">
                      {elem.description}
                    </h1>
                  </div>
                  <i className="material-symbols-rounded text-xl text-muted-foreground right-6 absolute">
                    chevron_right
                  </i>
                </div>
              </Link>
            );
          })}
        </div>
      </div>
    </div>
  );
}

export default Packages;
