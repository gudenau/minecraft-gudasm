# gudASM

The ~~worst~~ best thing to happen to Fabric modding.

#### TL;DR

It allows you to do direct bytecode manipulation and aims to make it "easy". You should only use this when [mixins](https://github.com/SpongePowered/Mixin/wiki) really can't do what you need.

#### What can this do?

Anything you want it to, since it allows you to edit classes at the most basic level.

Just be mindful of what classes you load and things should work fine.

#### Features

- Raw class transformations
- A large collection of helper methods
- @ForceBootstrap and @ForceInline for when you need ~~to break things~~ speed.
- Custom entry point

#### How to use

Create a class the implements `AsmInitializer` and put it in your mod config file as an entrypoint, something like:
```JSON
{
  ...
  "entrypoints": {
    "gud_asm": [
      "com.example.mod.ILikeBreakingThings"
    ]
  },
  ...
}
```

Then implement the `AsmInitializer.onInitializeAsm()` method and do something like this:
```Java
package com.example.mod;

import net.gudenau.asm.api.v0.AsmInitializer;
import net.gudenau.asm.api.v0.AsmRegistry;

public class ILikeBreakingThings implements AsmInitializer{
    @Override
    public void onInitializeAsm(){
        AsmRegistry.getInstance().registerTransformer(new AmazingTransformer());
    }
}
```

The rest should be pretty straight forward.

Also, please don't combine `AsmInitializer` with other initializers. That is just asking for crashes.
