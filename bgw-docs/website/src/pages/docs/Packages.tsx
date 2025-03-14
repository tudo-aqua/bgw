import { Banner } from "@/components/ui/banner";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
} from "@/components/ui/breadcrumb";
import { useDocsStore } from "@/stores/docsStore";
import { Link, useLocation } from "react-router-dom";
import { useEffect } from "react";
import { Badge } from "@/components/ui/badge";

function Packages({ packages }: { packages: any }) {
  const { setSecondarySidebar } = useDocsStore();

  const location = useLocation();

  useEffect(() => {
    const sidebarItems = [
      {
        title: "Packages (GUI)",
        url: location.pathname,
        items: Object.keys(packages)
          .map((e) => {
            return packages[e];
          })
          .filter((elem: any) => elem.module === "bgw-gui")
          .map((elem: any) => {
            return {
              title: elem.title,
              url: `/docs/${elem.package}`,
            };
          }),
      },
      {
        title: "Packages (Network)",
        url: location.pathname,
        items: Object.keys(packages)
          .map((e) => {
            return packages[e];
          })
          .filter((elem: any) => elem.module === "bgw-net")
          .map((elem: any) => {
            return {
              title: elem.title,
              url: `/docs/${elem.package}`,
            };
          }),
      },
    ];

    setSecondarySidebar(sidebarItems);
  }, [location.pathname]);

  return (
    <div className="flex justify-center w-full">
      <div className="px-10 2xl:max-w-[1600px] pt-5 mb-10 w-full component max-2xl:px-7">
        <Breadcrumb className="mb-10">
          <BreadcrumbList>
            <BreadcrumbItem>
              <BreadcrumbLink to={`/docs/`}>Reference</BreadcrumbLink>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>
        <div className="flex flex-col gap-10">
          <div className="flex flex-col gap-5">
            {Object.keys(packages)
              .map((e) => {
                return packages[e];
              })
              .filter((elem: any) => elem.module === "bgw-gui")
              .map((elem: any) => {
                return (
                  <Link to={"/docs/" + elem.package} className="group">
                    <div className="relative flex items-center gap-5 px-4 py-4 border-none bg-muted/50 rounded-xl docs-banner max-xl:!px-5">
                      <div className="flex items-center gap-5 max-xl:hidden">
                        <i className="flex items-center justify-center p-4 rounded-lg material-symbols-rounded text-muted-foreground bg-muted aspect-square w-14 h-14">
                          {elem.icon}
                        </i>
                      </div>
                      <div>
                        <h4 className="text-lg font-bold transition-all group-hover:translate-x-1">
                          {elem.title}
                        </h4>
                        <h1 className="text-base transition-all duration-300 text-muted-foreground group-hover:translate-x-1 max-xl:mt-2 max-xl:text-justify">
                          {elem.description}
                        </h1>
                      </div>
                      <i className="absolute text-xl material-symbols-rounded text-muted-foreground right-6 max-xl:hidden">
                        chevron_right
                      </i>
                    </div>
                  </Link>
                );
              })}
          </div>
          <div className="flex flex-col gap-5">
            {Object.keys(packages)
              .map((e) => {
                return packages[e];
              })
              .filter((elem: any) => elem.module === "bgw-net")
              .map((elem: any) => {
                return (
                  <Link to={"/docs/" + elem.package} className="group">
                    <div className="relative flex items-center gap-5 px-4 py-4 border-none bg-muted/50 rounded-xl docs-banner max-xl:!px-5 w-full">
                      <div className="flex items-center gap-5 max-xl:hidden">
                        <Badge
                          variant={"constant"}
                          className="flex items-center justify-center p-4 text-2xl rounded-lg material-symbols-rounded aspect-square w-14 h-14"
                        >
                          {elem.icon}
                        </Badge>
                      </div>
                      <div>
                        <h4 className="text-lg font-bold transition-all group-hover:translate-x-1">
                          {elem.title}
                        </h4>
                        <h1 className="text-base transition-all duration-300 text-muted-foreground group-hover:translate-x-1 max-xl:mt-2 max-xl:text-justify">
                          {elem.description}
                        </h1>
                      </div>
                      <i className="absolute text-xl material-symbols-rounded text-muted-foreground right-6 max-xl:hidden">
                        chevron_right
                      </i>
                    </div>
                  </Link>
                );
              })}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Packages;
