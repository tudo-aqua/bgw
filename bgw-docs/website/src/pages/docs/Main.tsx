import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import CodeTab from "./CodeTab";
import { layoutMap } from "@/lib/utils";
import { useState } from "react";
import { Badge } from "@/components/ui/badge";

const quickstartCode = `
implementation(
  group = "tools.aqua", 
  name = "bgw-gui", 
  version = "0.10"
)`;

function Main() {
  let [threeRandomLayoutKeys] = useState(
    Object.keys(layoutMap)
      .sort(() => 0.5 - Math.random())
      .slice(0, 3)
      .sort()
  );

  return (
    <div className="flex justify-center w-full">
      <div className="px-10 2xl:max-w-[1200px] pt-5 mb-10 w-full relative max-2xl:px-7 max-2xl:mb-0">
        <div className="mt-10 max-xl:mt-4">
          <h2 className="mb-2 text-4xl font-bold max-xl:text-3xl">
            Create board games in Kotlin
          </h2>
          <p className="text-base text-muted-foreground">
            BoardGameWork is a framework for creating 2D board game applications
            in Kotlin.
          </p>
        </div>
        <div className="grid grid-cols-2 gap-6 mt-6 mb-16 max-2xl:grid-cols-1">
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl max-xl:hidden">
            <h2 className="font-bold text-2xl mb-1 pl-0.5">
              Quickstart with Gradle
            </h2>
            <p className="mb-4 text-sm text-muted-foreground">
              Add the following dependency to your project's build.gradle.kts.
            </p>
            <CodeTab code={quickstartCode} copy={true}></CodeTab>
            <p className="gap-1 mt-4 text-sm text-muted-foreground">
              <span>Further configuration options can be found in the </span>
              <Link to="/guides" className="text-bgw-blue">
                Guides
              </Link>
              <span> section.</span>
            </p>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-bgw-blue/10 text-bgw-blue rounded-xl group">
            <i className="material-symbols-rounded text-[20rem] absolute -right-24 -top-16 rotate-12 opacity-[3%]">
              book_5
            </i>
            <h2 className="font-bold text-2xl mb-8 pl-0.5 max-2xl:text-lg">
              Create your first board game with BoardGameWork
            </h2>
            <img
              src="/bgw/guides/images/assets/doa.png"
              className="absolute transition-all duration-500 aspect-video w-96 rounded-xl -left-12 -bottom-6 group-hover:rotate-3 group-hover:-left-9 max-2xl:hidden"
            />
            <Link
              to="/guides"
              className="absolute z-10 bottom-8 right-8 max-2xl:relative max-2xl:bottom-0 max-2xl:right-0"
            >
              <Button variant="function" className="flex gap-2">
                <p>Start Guide</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden text-purple-500 border-none bg-purple-500/10 rounded-xl">
            <i className="material-symbols-rounded text-[20rem] absolute -right-24 -top-16 rotate-12 opacity-[3%]">
              docs
            </i>
            <h2 className="font-bold text-lg mb-8 pl-0.5">
              Discover the entirety of BoardGameWork's API endpoints to improve
              your game
            </h2>
            <Link to="/docs" className="relative z-10">
              <Button variant="class" className="flex gap-2">
                <p>Visit Reference</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-bgw-green/10 text-bgw-green rounded-xl">
            <i className="material-symbols-rounded text-[20rem] absolute -right-24 -top-16 rotate-12 opacity-[3%]">
              draw_abstract
            </i>
            <h2 className="font-bold text-lg mb-8 pl-0.5">
              Prototype scenes and components in the BoardGameWork Playground
            </h2>
            <Link to="/playground" className="relative z-10">
              <Button variant="property" className="flex gap-2">
                <p>Open Playground</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
            <Badge
              variant="property"
              className="absolute text-xs font-semibold right-8 top-9 max-xl:hidden"
            >
              Alpha
            </Badge>
          </fieldset>
        </div>
        <div className="mt-10">
          <h2 className={`font-bold text-2xl mb-2`}>Explore APIs</h2>
          <p className="text-base text-muted-foreground">
            Discover different API endpoints providing a wide range of features
            and functionalities.
          </p>
        </div>
        <div className="grid grid-cols-4 gap-6 mt-6 mb-16 max-lg:grid-cols-2 max-sm:grid-cols-1">
          {threeRandomLayoutKeys.map((key) => {
            let layout = layoutMap[key];
            return (
              <Link
                to={`/docs/${layout.package}`}
                key={key}
                className="relative z-10"
              >
                <fieldset className="relative flex flex-col items-center justify-center px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
                  <i className="material-symbols-rounded text-[4rem] pb-14 text-muted-foreground">
                    {layout.icon}
                  </i>
                  <Button
                    variant="primary"
                    className="absolute flex justify-between h-12 gap-2 bottom-2 left-2 right-2 bg-secondary"
                  >
                    <p className="pl-2">{layout.title}</p>
                    <i className="text-base material-symbols-rounded">
                      arrow_forward
                    </i>
                  </Button>
                </fieldset>
              </Link>
            );
          })}
          <Link to="/docs/" className="relative z-10">
            <fieldset className="relative flex flex-col items-center justify-center px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
              <i className="material-symbols-rounded text-[4rem] pb-14 text-muted-foreground">
                topic
              </i>
              <Button
                variant="primary"
                className="absolute flex justify-between h-12 gap-2 bottom-2 left-2 right-2 bg-secondary"
              >
                <p className="pl-2">More APIs</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </fieldset>
          </Link>
        </div>
        <div className="mt-10">
          <h2 className={`font-bold text-2xl mb-2`}>Community & Resources</h2>
          <p className="text-base text-muted-foreground">
            Join the BoardGameWork community and explore additional resources to
            enhance your development.
          </p>
        </div>
        <div className="grid grid-cols-2 gap-6 mt-6 mb-16 max-2xl:grid-cols-1">
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
            <i className="material-symbols-rounded text-[16rem] absolute -right-28 -top-10 rotate-12 opacity-[3%]">
              award_star
            </i>
            <h2 className="font-bold text-xl mb-6 pl-0.5">
              Star BoardGameWork on GitHub
            </h2>
            <Link
              to="https://github.com/tudo-aqua/bgw"
              className="relative z-10"
            >
              <Button variant="interface" className="flex gap-2">
                <p>View Repository</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
            <i className="material-symbols-rounded text-[16rem] absolute -right-28 -top-10 rotate-12 opacity-[3%]">
              grid_view
            </i>
            <h2 className="font-bold text-xl mb-6 pl-0.5">
              Checkout additional example projects
            </h2>
            <Link
              to="https://github.com/tudo-aqua/bgw/tree/main/bgw-examples"
              className="relative z-10"
            >
              <Button variant="interface" className="flex gap-2">
                <p>All Examples</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
            <i className="material-symbols-rounded text-[16rem] absolute -right-28 -top-10 rotate-12 opacity-[3%]">
              monitor_heart
            </i>
            <h2 className="font-bold text-xl mb-6 pl-0.5">
              Help us improve BoardGameWork
            </h2>
            <Link
              to="https://github.com/tudo-aqua/bgw/issues/new/choose"
              className="relative z-10"
            >
              <Button variant="interface" className="flex gap-2">
                <p>Report a Bug</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
          <fieldset className="relative px-8 py-8 overflow-hidden border-none bg-muted/50 rounded-xl">
            <i className="material-symbols-rounded text-[16rem] absolute -right-28 -top-10 rotate-12 opacity-[3%]">
              newspaper
            </i>
            <h2 className="font-bold text-xl mb-6 pl-0.5">
              View release notes of the latest version
            </h2>
            <Link
              to="https://github.com/tudo-aqua/bgw/releases"
              className="relative z-10"
            >
              <Button variant="interface" className="flex gap-2">
                <p>See Changelog</p>
                <i className="text-base material-symbols-rounded">
                  arrow_forward
                </i>
              </Button>
            </Link>
          </fieldset>
        </div>
      </div>
    </div>
  );
}

export default Main;
