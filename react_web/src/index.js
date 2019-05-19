import dva from 'dva';
import './index.css';
// 1. Initialize
const app = dva();

// 2. Plugins
// app.use({});

// 3. Model
app.model(require('./models/AdModel').default);
app.model(require('./models/CateTable').default);
app.model(require('./models/CateFunnel').default);
app.model(require('./models/CateRetain').default);
app.model(require('./models/CateAgeGenderDis').default);

// 4. Router
app.router(require('./router').default);

// 5. Start
app.start('#root');
