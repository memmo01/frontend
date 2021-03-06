// @flow
import type { SlotOnloadEvent } from 'commercial-control/types';

import { Advert } from 'commercial-control/modules/dfp/Advert';
import { getAdvertById } from 'commercial-control/modules/dfp/get-advert-by-id';
import { postMessage } from 'commercial-control/modules/messenger/post-message';

const host = `${window.location.protocol}//${window.location.host}`;

/* This is for native ads. We send two pieces of information:
   - the ID of the iframe into which this ad is embedded. This is currently
     the only way to link an incoming message to the iframe it is "coming from"
   - the HOST of the parent frame. Again, inside the embedded document there is
     no way to know if we are running the site in production, dev or local mode.
     But, this information is necessary in the window.postMessage call, and so
     we resort to sending it as a token of welcome :)
*/
const onLoad = (event: SlotOnloadEvent) => {
    const advert: ?Advert = getAdvertById(event.slot.getSlotElementId());
    if (!advert) {
        return;
    }

    const iframe = advert.node.getElementsByTagName('iframe')[0];
    postMessage(
        {
            id: iframe.id,
            host,
        },
        iframe.contentWindow
    );
};

export default onLoad;
